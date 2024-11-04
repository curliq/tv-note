package com.free.tvtracker

import io.sentry.kotlin.multiplatform.Sentry

object ComposeStartup {
    fun initSentry(sentryDsnKey:String) {
        Sentry.init { options ->
            options.dsn = sentryDsnKey
        }
    }
}
