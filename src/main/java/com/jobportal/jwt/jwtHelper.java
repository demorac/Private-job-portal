package com.jobportal.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class jwtHelper {

    // Use a valid Base64-encoded secret key
    private static final String SECRET_KEY = "eW91ci0yNTYtYml0LXNlY3JldC15b3VyLTI1Ni1iaXQtc2VjcmV0LXlvdXItMjU2LWJpdC1zZWNyZXQ=";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    // Generate JWT Token
   public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
    
    // ✅ Ensure all necessary fields are included
    claims.put("id", customUserDetails.getId());
    claims.put("name", customUserDetails.getName());
    claims.put("accountType", customUserDetails.getAccountType());
    claims.put("profileId", customUserDetails.getProfileId());

    return Jwts.builder()
            .setClaims(claims) // ✅ Pass claims here (this was missing!)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
}


    // Validate Token
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Extract Username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract Expiration Date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Check if Token is Expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract Claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract All Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}