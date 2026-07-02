package com.LibGO.Library.security;

import com.LibGO.Library.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String email, User. UserType userType){

        return Jwts.builder().subject(email).claim("roles", userType.name()).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expiration)).signWith(getSigningKey()).compact();

    }

    public String extractEmail(String token){

        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();

    }

    public String extractRoles(String token){

        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().get("roles", String.class);

    }

    public Boolean validateToken(String token){
        try {
            return extractEmail(token) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
