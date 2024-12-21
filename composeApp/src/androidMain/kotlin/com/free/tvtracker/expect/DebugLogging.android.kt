package com.free.tvtracker.expect

import android.util.Log

actual fun logW(tag: String, message: String) {
    Log.d(tag, message)
}

actual fun logE(tag: String, error: Throwable) {
    Log.e(tag, error.message, error)
}

actual fun logD(tag: String, message: String) {
    Log.d(tag, message)
}
