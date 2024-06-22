package com.free.tvtracker.expect.data

import platform.Foundation.NSLocale
import platform.Foundation.localizedStringForCountryCode
import platform.Foundation.regionCode

actual open class LocationService {
    actual fun platformCountryCode(): String {
        return NSLocale.new()?.regionCode ?: "US" //todo test
    }

    actual fun countryName(countryCode: String): String {
        return NSLocale.new()?.localizedStringForCountryCode(countryCode) ?: "-" //todo test
    }
}
