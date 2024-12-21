package com.free.tvtracker.expect

expect fun logW(tag: String, message: String)
expect fun logE(tag: String, error: Throwable)
expect fun logD(tag: String, message: String)
