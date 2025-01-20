package com.zerozero.core.util;

import com.zerozero.auth.exception.AuthenticationErrorCode;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUtil {

  @Value("${jwt.secretKey}")
  private String secretKey;

  @Value("${jwt.expiration}")
  private long jwtExpiration;

  @Value("${jwt.refresh-token.expiration}")
  private long refreshExpiration;

  public UUID extractUserId(String token) {
    return UUID.fromString(extractClaim(token, claims -> claims.get("userId", String.class)));
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public AccessToken generateAccessToken(User user) {
    String accessToken = Jwts.builder()
        .setClaims(Map.of(
            "userId", user.getId(),
            "email", user.getEmail()
        ))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
    return AccessToken.of(accessToken);
  }

  public RefreshToken generateRefreshToken(User user) {
    String refreshToken = Jwts.builder()
        .setClaims(Map.of(
            "userId", user.getId(),
            "email", user.getEmail()
        ))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
    return RefreshToken.of(refreshToken);
  }

  public void validateExpiration(String token) {
    if (isTokenExpired(token)) {
      throw AuthenticationErrorCode.EXPIRED_TOKEN.toException();
    }
  }

  public boolean isTokenValid(String token, User user) {
    final UUID userId = extractUserId(token);
    return (userId.equals(user.getId())) && !isTokenExpired(token);
  }

  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts
          .parserBuilder()
          .setSigningKey(getSignInKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      throw AuthenticationErrorCode.NOT_DEFINE_TOKEN.toException();
    }
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
