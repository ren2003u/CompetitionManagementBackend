package com.example.demo.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final String secret = "apple";  // Replace with your own secret
    private final int jwtExpirationInMs = 3600000; // 1 hour - you can modify this as per your requirements

    // Generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // while creating the token -
    // 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    // 3. According to JWS Compact
    // Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    // compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    // Validate the token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Retrieve username from JWT token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Retrieve expiration date from JWT token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // To retrieve any information from token we will need the secret key
    // Question:I am curious if we skip the login part and fabricate a similar format token.
    // Then we fill this fake token into our request header and try to access those protected APIs.
    // how do the SpringSecurity knows the token is invalid?
    // Answer:Spring Security uses the secret key to validate the JWT token.
    // As part of the validation process, Spring Security checks that the token was signed using the same secret key that's stored in the server.
    // If the token was not signed with the correct secret key, it will be rejected.
    // Let's take a look at the method where this happens in your JwtUtil class:
    // Here, Jwts.parser().setSigningKey(secret) creates a JWT parser that will use the secret key to validate the token.
    // parseClaimsJws(token) parses the JWT token, validates its signature, and returns the claims inside the token.
    // If the signature is not valid (i.e., the token was not signed with the correct secret key), parseClaimsJws will throw an exception, and the token will be rejected.
    // This is why it's very important to keep your secret key private. Anyone who knows the secret key can generate valid tokens, which would be a serious security risk.
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // Check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}
