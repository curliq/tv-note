package com.free.tvtracker.expect

actual fun logW(tag: String, message: String) {
    println("[Warn] $tag: $message\n")
}

actual fun logE(tag: String, error: Throwable) {
    println("[Error] $tag: $error\n")
}

actual fun logD(tag: String, message: String) {
    println("[Debug] $tag: $message\n")
}
