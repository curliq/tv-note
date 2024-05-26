package com.free.tvtracker.security

import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.features.user.data.UserEntity
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
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

    fun generate(userDetails: UserEntity): String? {
        val additionalClaims: MutableMap<String, Any> = mutableMapOf(CLAIMS_EXTRAS_ROLE to userDetails.role.key)
        return try {
            Jwts.builder()
                .claims()
                .subject(userDetails.id.toString())
                .issuedAt(Date(System.currentTimeMillis()))
                .expiration(getAccessTokenExpiration())
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

    private fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    private fun getAccessTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)

    private fun getAllClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
}
