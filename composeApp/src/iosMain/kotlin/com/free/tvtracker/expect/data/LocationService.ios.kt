package com.free.tvtracker.expect.data

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.localizedStringForCountryCode
import platform.Foundation.regionCode

actual open class LocationService {
    actual fun platformCountryCode(): String {
        println("platformCountryCode: ${NSLocale.currentLocale.regionCode}")
        return NSLocale.currentLocale.regionCode ?: "US" //todo test
    }

    actual fun countryName(countryCode: String): String {
        println("countryName: ${NSLocale.currentLocale.localizedStringForCountryCode(countryCode)}")
        return NSLocale.currentLocale.localizedStringForCountryCode(countryCode) ?: "-" //todo test
    }

    actual fun languageCode(): String {
        println("languageCode: ${NSLocale.currentLocale.languageCode()}")
        return NSLocale.currentLocale.languageCode()
    }
}
