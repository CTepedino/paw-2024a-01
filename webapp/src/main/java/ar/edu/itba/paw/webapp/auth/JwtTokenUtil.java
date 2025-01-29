package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Component
public class JwtTokenUtil {

    private static final long EXPIRATION_TIME = TimeUnit.DAYS.toMillis(7);

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenUtil(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String generateToken(User user){
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities());

        Date now = new Date();
        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public UserDetails parseToken(String jws){
        try {
            final Claims claims = getClaims(jws);
            if (isTokenExpired(claims)){
                return null;
            }
            return userDetailsService.loadUserByUsername(getUsername(jws));
        } catch (Exception e){
            return null;
        }
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String getUsername(String token){
        return getClaims(token).getSubject();
    }

    private boolean isTokenExpired(Claims claims){
        return claims.getExpiration().before(new Date());
    }

    private Authentication getAuthentication(String token, UserDetails userDetails){
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
