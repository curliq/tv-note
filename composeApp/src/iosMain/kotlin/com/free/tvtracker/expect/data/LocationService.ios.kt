package com.free.tvtracker.expect.data

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.localizedStringForCountryCode
import platform.Foundation.regionCode

actual open class LocationService {
    actual fun platformCountryCode(): String {
        return NSLocale.currentLocale.regionCode ?: "US"
    }

    actual fun countryName(countryCode: String): String {
        return NSLocale.currentLocale.localizedStringForCountryCode(countryCode) ?: "-"
    }

    actual fun languageCode(): String {
        return NSLocale.currentLocale.languageCode()
    }
}
