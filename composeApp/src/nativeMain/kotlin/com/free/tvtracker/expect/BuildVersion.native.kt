package com.free.tvtracker.expect

import platform.Foundation.NSBundle

actual class BuildVersion actual constructor() {
    actual fun name(): String =
        NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "Unknown"

    actual fun code(): Int =
        (NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleVersion") as? String)?.toIntOrNull() ?: 0
}
