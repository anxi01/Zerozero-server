package com.zerozero.core.util

import com.zerozero.auth.exception.AuthErrorType
import com.zerozero.auth.exception.AuthException
import com.zerozero.user.domain.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtUtil(
    @Value("\${jwt.secretKey}")
    private val secretKey: String,

    @Value("\${jwt.expiration}")
    private val jwtExpiration: Long,

    @Value("\${jwt.refresh-token.expiration}")
    private val refreshExpiration: Long
) {

    fun extractUserId(token: String): UUID {
        return UUID.fromString(extractClaim(token) { claims -> claims.get("userId", String::class.java) })
    }

    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    fun generateAccessToken(user: User): String {
        return Jwts.builder()
            .setClaims(
                mapOf(
                    "userId" to user.id,
                    "email" to user.email
                )
            )
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(signInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateRefreshToken(user: User): String {
        return Jwts.builder()
            .setClaims(
                mapOf(
                    "userId" to user.id,
                    "email" to user.email
                )
            )
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + refreshExpiration))
            .signWith(signInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateExpiration(token: String) {
        if (isTokenExpired(token)) {
            throw AuthException(AuthErrorType.EXPIRED_TOKEN)
        }
    }

    fun isTokenValid(token: String, user: User): Boolean {
        val userId = extractUserId(token)
        return (userId == user.id) && !isTokenExpired(token)
    }

    fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { obj -> obj.expiration }
    }

    private fun extractAllClaims(token: String): Claims {
        try {
            return Jwts
                .parserBuilder()
                .setSigningKey(signInKey())
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            throw AuthException(AuthErrorType.NOT_DEFINE_TOKEN)
        }
    }

    private fun signInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}
