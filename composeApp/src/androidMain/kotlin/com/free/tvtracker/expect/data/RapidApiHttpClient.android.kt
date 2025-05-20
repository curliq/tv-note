package com.free.tvtracker.expect.data

import com.free.tvtracker.BuildConfig

actual fun getRapidApiToken(): String {
    return BuildConfig.RAPID_API_IMDB_TOKEN
}
