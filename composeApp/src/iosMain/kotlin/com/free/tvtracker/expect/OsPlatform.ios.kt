package com.free.tvtracker.expect

actual class OsPlatform {
    actual enum class Platform { Android, IOS, Other }

    actual fun get(): Platform = Platform.IOS
}
