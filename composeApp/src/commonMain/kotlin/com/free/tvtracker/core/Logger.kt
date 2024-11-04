package com.free.tvtracker.core

import com.free.tvtracker.expect.logE
import com.free.tvtracker.expect.logW
import io.sentry.kotlin.multiplatform.Sentry

class Logger {
    fun w(message: String, tag: String = "NOTAG") {
        logW(tag, message)
        Sentry.captureMessage(message)
    }

    fun e(error: Throwable, tag: String = "NOTAG") {
        logE(tag, error)
        Sentry.captureException(error)
    }
}
