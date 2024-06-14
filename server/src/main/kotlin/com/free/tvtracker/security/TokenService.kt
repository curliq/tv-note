package com.free.tvtracker.security

import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.features.user.data.UserEntity
import com.free.tvtracker.features.user.data.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

@Service
class TokenService(
    private val logger: TvtrackerLogger,
    private val jwtProperties: JwtProperties,
) {
    private val secretKey = Keys.hmacShaKeyFor(jwtProperties.key.toByteArray())

    companion object {
        private const val CLAIMS_EXTRAS_ROLE = "role"
    }

    fun generate(userId: Int, role: UserRole): String? {
        val additionalClaims: MutableMap<String, Any> = mutableMapOf(CLAIMS_EXTRAS_ROLE to role.key)
        return try {
            Jwts.builder()
                .claims()
                .subject(userId.toString())
                .issuedAt(Date(System.currentTimeMillis()))
                .expiration(Date.from(Instant.ofEpochSecond(253402300800))) // 10000/01/01 00:00:00
                .add(additionalClaims)
                .and()
                .signWith(secretKey)
                .compact()
        } catch (e: Exception) {
            logger.get.debug("unable to create token: ", e)
            null
        }
    }

    fun isValid(token: String): Boolean {
        return extractUserId(token) != null && !isExpired(token)
    }

    fun extractUserId(token: String): Int? =
        getAllClaims(token).subject?.toInt()

    fun extractRole(token: String): String = getAllClaims(token)[CLAIMS_EXTRAS_ROLE].toString()

    /**
     * expiration should be 1 billion years away, if this is ever true then something went wrong
     */
    private fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    private fun getAllClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
}
