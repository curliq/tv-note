package com.free.tvtracker.expect

//import io.sentry.Sentry
import com.free.tvtracker.BuildConfig
import io.sentry.kotlin.multiplatform.Sentry

actual fun logSentry(error: Throwable) {
    Sentry.captureException(error)
}

actual fun logSentry(message: String) {
    Sentry.captureMessage(message)
}

actual fun initSentry(dsn: String) {
    Sentry.init { options ->
        options.dsn = dsn
        options.environment = if (BuildConfig.DEBUG) "development" else "production"
    }
}
