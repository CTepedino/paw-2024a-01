package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Component
public class JwtTokenUtil {

    private static final long EXPIRATION_TIME = TimeUnit.HOURS.toMillis(2);
    private static final long REFRESH_TOKEN_EXPIRATION = TimeUnit.DAYS.toMillis(31);


    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final SecretKey refreshTokenKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenUtil(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String generateToken(User user){
        Date now = new Date();
        return "Bearer " + Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(User user){
        Date now = new Date();
        return "Bearer " + Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION))
                .signWith(refreshTokenKey)
                .compact();
    }

    public UserDetails parseToken(String jws){
        try {
            final Claims claims = getClaims(secretKey, jws);
            if (isTokenExpired(claims)){
                return null;
            }
            return userDetailsService.loadUserByUsername(getUsername(secretKey, jws));
        } catch (Exception e){
            return null;
        }
    }

    public UserDetails parseRefreshToken(String refreshToken) {
        try {
            Claims claims = getClaims(refreshTokenKey, refreshToken);
            if (isTokenExpired(claims)){
                return null;
            }
            return userDetailsService.loadUserByUsername(getUsername(refreshTokenKey, refreshToken));
        } catch (Exception e) {
            return null;
        }
    }

    private Claims getClaims(SecretKey key, String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String getUsername(SecretKey key, String token){
        return getClaims(key, token).getSubject();
    }

    private boolean isTokenExpired(Claims claims){
        return claims.getExpiration().before(new Date());
    }

}
