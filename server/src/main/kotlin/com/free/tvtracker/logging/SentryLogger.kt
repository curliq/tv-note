package com.free.tvtracker.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.free.tvtracker.security.SessionService
import io.sentry.Sentry
import io.sentry.SentryOptions
import io.sentry.logback.SentryAppender
import io.sentry.protocol.User
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
 * This sends every Level.ERROR log from EVERY logback logger to Sentry
 */
@Component
class Sentry {

    @Value("\${sentry.dsn}")
    private val dsn: String = ""

    @Value("\${sentry.environment}")
    private val env: String? = null

    init {
        val context: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val sentryAppender = SentryAppender()
        sentryAppender.context = context
        val sentryOptions = SentryOptions()
        sentryOptions.dsn = dsn
        sentryOptions.environment = env
        sentryAppender.setOptions(sentryOptions)
        sentryAppender.name = "SENTRY"
        sentryAppender.setMinimumEventLevel(Level.ERROR)
        if (dsn.isBlank()) {
            sentryOptions.tags["dsn_missing"] = "true"
        }
        sentryAppender.start()
        context.getLogger("ROOT").addAppender(sentryAppender)
    }
}

/**
 * This sets the user ID on the Sentry object before the request is processed
 */
@Component
class SentryInterceptor(
    private val sessionService: SessionService,
    private val logger: TvtrackerLogger,
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val id = sessionService.getSessionUserIdOptional()
        val userId = id?.toString() ?: "anon"
        Sentry.setUser(User().apply {
            this.ipAddress = ""
            this.id = userId
        })
        logger.get.debug(
            "Setting user Id for Sentry: $userId"
        )
        return true
    }
}
