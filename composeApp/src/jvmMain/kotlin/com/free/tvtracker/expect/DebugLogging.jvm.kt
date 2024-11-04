package com.free.tvtracker.expect

actual fun logW(tag: String, message: String) {
    println("[W] $tag: $message")
}

actual fun logE(tag: String, error: Throwable) {
    println("[E] $tag: $error")
}
