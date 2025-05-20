package com.free.tvtracker.expect.data

import com.free.tvtracker.BuildConfig

actual fun getOmdbToken(): String {
    return BuildConfig.OMDB_TOKEN
}
