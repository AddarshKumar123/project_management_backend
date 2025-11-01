package project_management.Server.Util;

import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtility {


    private final Key key;
    private final long EXPIRATION;

    public JwtUtility(
            @Value("${SECRET}") String secret,
            @Value("${JWT_EXPIRATION_TIME}") long EXPIRATION
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.EXPIRATION = EXPIRATION;
    }

    public String generateToken(String email, String role){
        return Jwts.builder()
                .setSubject(email)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token){
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token){
        try{
            parseClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private Claims parseClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
