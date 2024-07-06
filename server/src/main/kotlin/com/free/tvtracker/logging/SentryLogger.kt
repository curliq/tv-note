package com.free.tvtracker.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import io.sentry.Sentry
import io.sentry.SentryOptions
import io.sentry.logback.SentryAppender
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SentryUnhandledExceptionInterceptor {
    @EventListener
    fun handleException(e: Exception) {
        Sentry.captureException(e)
    }
}

/**
 * This sends every Level.ERROR log from EVERY logback logger to Sentry
 */
@Component
class SentryLogbackInterceptor {
    @Value("\${sentry.dsn}")
    private val dsn: String? = null

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
        sentryAppender.start()
        context.getLogger("ROOT").addAppender(sentryAppender)
    }
}
