package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class BasicAuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    public BasicAuthFilter(final AuthenticationManager authenticationManager, final AuthenticationEntryPoint authenticationEntryPoint, final JwtTokenUtil jwtTokenUtil, final UserService userService){
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Basic ")){
            try {
                byte[] decoded;
                try {
                    decoded = Base64.decode(header.split(" ")[1].trim().getBytes(StandardCharsets.UTF_8));
                } catch (IllegalArgumentException e) {
                    throw new BadCredentialsException("Failed to decode basic authentication token");
                }

                String token = new String(decoded, StandardCharsets.UTF_8);

                int separator = token.indexOf(":");
                if (separator == -1) {
                    throw new BadCredentialsException("Invalid basic authentication token");
                }

                String email = token.substring(0, separator);
                final Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, token.substring(separator + 1))
                );

                userService.findByEmail(email).ifPresent(u -> response.setHeader("Authorization", jwtTokenUtil.generateToken(u)));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (AuthenticationException e){
                SecurityContextHolder.clearContext();
                authenticationEntryPoint.commence(request, response, e);
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
