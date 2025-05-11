package com.free.tvtracker.expect

import com.free.tvtracker.BuildConfig

actual class BuildVersion actual constructor() {
    actual fun name(): String = BuildConfig.VERSION_NAME
    actual fun code(): Int = BuildConfig.VERSION_CODE
}
