package com.free.tvtracker.utils

actual class OsPlatform {
    actual enum class Platform { Android, IOS, Other }

    actual fun get(): Platform = Platform.Other

}
