package com.free.tvtracker.security

import com.free.tvtracker.core.logging.TvtrackerLogger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val loggerr: TvtrackerLogger,
    private val tokenService: TokenService,
    private val sessionService: SessionService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader("Authorization")
        if (authHeader.doesNotContainBearerToken()) {
            filterChain.doFilter(request, response)
            loggerr.get.debug("Failed to authenticate: no bearer token")
            return
        }
        val jwtToken = authHeader!!.extractTokenValue()
        if (!tokenService.isValid(jwtToken)) {
            loggerr.get.debug("Failed to authenticate: token invalid")
            return
        }
        val id = tokenService.extractUserId(jwtToken)!!
        val role = tokenService.extractRole(jwtToken)
        if (SecurityContextHolder.getContext().authentication == null) {
            sessionService.updateSession(id, role, request)
        }
        filterChain.doFilter(request, response)
    }

    private fun String?.doesNotContainBearerToken() =
        this == null || !this.startsWith("Bearer ")

    private fun String.extractTokenValue() =
        this.substringAfter("Bearer ")
}
