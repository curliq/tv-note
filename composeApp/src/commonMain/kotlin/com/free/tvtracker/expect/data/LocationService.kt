package com.free.tvtracker.expect.data

class CachingLocationService : LocationService() {

    private var countryCode: String? = null

    fun getCountryCode(): String {
        if (countryCode != null) {
            return countryCode!!
        } else {
            countryCode = platformCountryCode()
            return countryCode!!
        }
    }

    fun countryName(): String {
        return super.countryName(getCountryCode())
    }
}

expect open class LocationService() {
    fun platformCountryCode(): String
    fun countryName(countryCode: String): String
}
