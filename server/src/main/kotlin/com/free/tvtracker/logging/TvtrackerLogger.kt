package com.free.tvtracker.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TvtrackerLogger {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    val get: Logger = logger
}

fun Logger.error(exception: Throwable) {
    this.error(exception.message ?: "Exception of type ${exception::class}", exception)
}
