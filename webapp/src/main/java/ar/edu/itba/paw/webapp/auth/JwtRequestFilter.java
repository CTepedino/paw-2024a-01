package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService us;

    @Autowired
    public JwtRequestFilter(final JwtTokenUtil jwtTokenUtil, final UserService us){
        this.jwtTokenUtil = jwtTokenUtil;
        this.us = us;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String jwtToken = authorizationHeader.substring(7);
            UserDetails userDetails = jwtTokenUtil.parseToken(jwtToken);

            if (userDetails != null && userDetails.isEnabled() && userDetails.isAccountNonLocked() && SecurityContextHolder.getContext().getAuthentication() == null){
                final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
                response.setHeader("Authorization", authorizationHeader);
            } else {
                userDetails = jwtTokenUtil.parseRefreshToken(jwtToken);
                if (userDetails != null && userDetails.isEnabled() && userDetails.isAccountNonLocked() && SecurityContextHolder.getContext().getAuthentication() == null){
                    final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    User user = us.findByEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);
                    response.setHeader("Authorization", jwtTokenUtil.generateToken(user));
                    response.setHeader("X-Refresh-Token", jwtTokenUtil.generateRefreshToken(user));
                }
            }
        }
        chain.doFilter(request, response);
    }
}
