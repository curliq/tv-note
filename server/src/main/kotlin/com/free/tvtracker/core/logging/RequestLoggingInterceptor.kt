package com.free.tvtracker.core.logging

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.util.Enumeration

@Component
class RequestLoggingInterceptor : HandlerInterceptor {
    private val logger: Logger = LoggerFactory.getLogger(RequestLoggingInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.info(
            "Request received: ${request.method} ${request.requestURI} ${getParameters(request)}"
        )
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        super.afterCompletion(request, response, handler, ex)
        logger.info(
            "Request completed: ${response.status}"
        )
    }

    private fun getParameters(request: HttpServletRequest): String {
        val posted = StringBuilder()
        val e: Enumeration<*>? = request.parameterNames
        if (e != null) {
            posted.append(" parameters: ")
        }
        while (e!!.hasMoreElements()) {
            if (posted.length > 1) {
                posted.append("&")
            }
            val curr = e.nextElement() as String
            posted.append(curr).append("=")
            if ((curr.contains("password")
                    || curr.contains("pw")
                    || curr.contains("pwd"))
            ) {
                posted.append("*****")
            } else {
                posted.append(request.getParameter(curr))
            }
        }
        val ip = request.getHeader("X-FORWARDED-FOR")
        val ipAddr = if ((ip == null)) getRemoteAddr(request) else ip
        if (ipAddr.isNotEmpty()) {
            posted.append(" ip=$ipAddr")
        }
        return posted.toString()
    }

    private fun getRemoteAddr(request: HttpServletRequest): String {
        val ipFromHeader = request.getHeader("X-FORWARDED-FOR")
        if (ipFromHeader != null && ipFromHeader.isNotEmpty()) {
            return ipFromHeader
        }
        return request.remoteAddr
    }
}
