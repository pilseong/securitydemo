package com.example.securitydemo.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtTokenProvider {
  @Value("${app.jwt-secret")
  private String jwtSecret;

  @Value("${app.jwt-expiration-milliseconds}")
  private int jwtExpirationInMilliseconds;

  // private final String baseKey = "thisisdummykeythisisdummykeythisisdummykeythisisdummykeythisisdummykey";
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;


  // generate token
  public String generateToken(Authentication authentication) {
    String username = authentication.getName();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMilliseconds);

    String token = Jwts.builder()
      .setSubject(username)
      .setIssuedAt(new Date())
      .setExpiration(expireDate)
      .signWith(createKey(), signatureAlgorithm)
      .compact();

    return token;
  }

  private Key createKey() {
    byte[] apiKeySecretBytes = Base64.getEncoder().encode(jwtSecret.getBytes());
    // Key signingKey = Keys.hmacShaKeyFor(apiKeySecretBytes);
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    return signingKey;
  }

  // get username from token
  public String getUsernameFromJwt(String token) {
    Claims claims = Jwts.parserBuilder()
      .setSigningKey(Base64.getEncoder().encode(jwtSecret.getBytes()))
      .build()
      .parseClaimsJws(token)
      .getBody();

    System.out.println();

    return claims.getSubject();
  }

  // validate JWT token
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
      .setSigningKey(Base64.getEncoder().encode(jwtSecret.getBytes()))
      .build()
      .parseClaimsJws(token);
      
      return true;
    }  catch (MalformedJwtException ex) {
      // throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
      throw new RuntimeException("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      // throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
      throw new RuntimeException("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      // throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
      throw new RuntimeException("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      // throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWt claims string is empty");
      throw new RuntimeException("JWt claims string is empty");
    }
  }
}
