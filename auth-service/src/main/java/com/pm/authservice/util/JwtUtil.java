package com.pm.authservice.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

// register this class as a java bean
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private final Key secreatKey;

    public JwtUtil(@Value("${jwt.secret}") String secret){
        // you have to add a secreat key that is suitable for base64
//        byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
            log.info("secret key: {}", secret);
        this.secreatKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

//        this.secreatKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String role){
        return Jwts.builder()
                .subject(email)
                .claim("role", role) // this is not a must needed one
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(secreatKey)
                .compact();
    }

    public void validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith((SecretKey) secreatKey)
                    .build()
                    .parseSignedClaims(token);

        }catch(SignatureException e ){
            throw new JwtException("Invalid JWT signature");

        } catch (JwtException e){
            throw new JwtException("Invalid JWT token");
        }
//        catch (Exception e){
//            throw new JwtException("JWT token is expired or invalid");
//        }
    }
}
