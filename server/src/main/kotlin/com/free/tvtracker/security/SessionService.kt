package com.free.tvtracker.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service

@Service
class SessionService {
    fun updateSession(userId: Int, role: String, request: HttpServletRequest) {
        val authorities = mutableListOf<GrantedAuthority>()
        val authority = GrantedAuthority { role }
        authorities.add(authority)
        val authenticatedUser = UsernamePasswordAuthenticationToken(userId, null, authorities)
        authenticatedUser.details = WebAuthenticationDetailsSource().buildDetails(request)
        println("user: $userId, role: $role, request: $request")
        SecurityContextHolder.getContext().authentication = authenticatedUser
    }

    fun getSession(): Authentication {
        return SecurityContextHolder.getContext().authentication
    }

    fun getSessionUserId(): Int {
        return getSession().principal as Int
    }
}
