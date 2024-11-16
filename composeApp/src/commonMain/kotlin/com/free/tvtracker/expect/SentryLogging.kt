package com.free.tvtracker.expect

expect fun logSentry(error: Throwable)

expect fun logSentry(message: String)

expect fun initSentry(dsn: String)
