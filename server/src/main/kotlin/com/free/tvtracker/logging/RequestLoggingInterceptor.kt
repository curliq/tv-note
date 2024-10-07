package com.free.tvtracker.logging

import com.free.tvtracker.logging.RequestResponseLogInterceptor.Companion.getParameters
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerInterceptor
import java.util.Enumeration

/**
 * This logs a request after it's processed
 */
@Component
class RequestResponseLogInterceptor : OncePerRequestFilter() {

    private val requestLogger: Logger = LoggerFactory.getLogger(RequestLoggingInterceptor::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Continue with the next filter in the chain
        filterChain.doFilter(request, response)

        // Log the response status after the request has been processed
        requestLogger.debug("Request completed: ${response.status}")
        if (response.status >= 400) {
            val error = "Request unsuccessful: ${request.method} ${request.requestURI}${getParameters(request)}"
            requestLogger.error(Throwable(error))
        }
    }

    companion object {
        internal fun getParameters(request: HttpServletRequest): String {
            val posted = StringBuilder()
            val params: Enumeration<*>? = request.parameterNames
            if (params?.hasMoreElements() == true) {
                posted.append(" parameters: ")
            } else {
                return ""
            }
            while (params.hasMoreElements()) {
                if (posted.length > 1) {
                    posted.append("&")
                }
                val curr = params.nextElement() as String
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
            return posted.toString()
        }
    }
}

/**
 * This logs a request when it's received and before it's processed
 */
@Component
class RequestLoggingInterceptor : HandlerInterceptor {

    private val logger: Logger = LoggerFactory.getLogger(RequestLoggingInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.info(
            "Request received: ${request.method} ${request.requestURI}${getParameters(request)}"
        )
        return true
    }
}
