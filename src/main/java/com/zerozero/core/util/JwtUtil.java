package com.zerozero.core.util;

import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
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

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public AccessToken generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    if (extraClaims == null) {
      extraClaims = new HashMap<>();
    }
    String accessToken = Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
    return AccessToken.of(accessToken);
  }

  public RefreshToken generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    if (extraClaims == null) {
      extraClaims = new HashMap<>();
    }
    String refreshToken = Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
    return RefreshToken.of(refreshToken);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  public boolean isTokenExpired(String token) {
    try {
      return extractExpiration(token).before(new Date());
    } catch (Exception e) {
      return true;
    }
  }

  public static boolean isJWT(String token) {
    final String JWT_REGEX = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$";
    final Pattern JWT_PATTERN = Pattern.compile(JWT_REGEX);
    if (token == null) {
      return false;
    }
    return JWT_PATTERN.matcher(token).matches();
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
