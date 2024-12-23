package com.free.tvtracker.expect

import io.sentry.kotlin.multiplatform.Sentry
import kotlin.experimental.ExperimentalNativeApi

actual fun logSentry(error: Throwable) {
    Sentry.captureException(error)
}

actual fun logSentry(message: String) {
    Sentry.captureMessage(message)
}

@OptIn(ExperimentalNativeApi::class)
actual fun initSentry(dsn: String) {
    Sentry.init { options ->
        options.dsn = dsn
        options.environment = if (Platform.isDebugBinary) "development" else "production"
    }
}
