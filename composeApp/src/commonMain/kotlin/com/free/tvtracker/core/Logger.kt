package com.free.tvtracker.core

import com.free.tvtracker.expect.logD
import com.free.tvtracker.expect.logE
import com.free.tvtracker.expect.logSentry
import com.free.tvtracker.expect.logW

class Logger {
    fun w(message: String, tag: String = "NOTAG") {
        logW(tag, message)
        logSentry(message)
    }

    fun e(error: Throwable, tag: String = "NOTAG") {
        logE(tag, error)
        logSentry(error)
    }

    fun d(message: String, tag: String = "NOTAG") {
        logD(tag, message)
    }
}
