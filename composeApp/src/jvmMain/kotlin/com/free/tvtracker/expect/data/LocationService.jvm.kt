package com.free.tvtracker.expect.data

actual open class LocationService {
    actual fun platformCountryCode(): String {
        return "US"
    }

    actual fun countryName(countryCode: String): String {
        return "america"
    }
}
