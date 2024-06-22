package com.free.tvtracker.expect.data

import android.content.Context
import android.telephony.TelephonyManager

actual class LocationService {
    actual fun getCountryCode(): String {
        val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCodeValue = tm.networkCountryIso
    }
}
