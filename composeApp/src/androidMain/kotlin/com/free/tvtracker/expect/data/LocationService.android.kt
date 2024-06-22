package com.free.tvtracker.expect.data

import android.content.Context
import android.telephony.TelephonyManager
import com.free.tvtracker.AndroidApplication
import java.util.Locale

actual open class LocationService {
    actual fun platformCountryCode(): String {
        val tm = AndroidApplication.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkCountryIso
    }

    actual fun countryName(countryCode: String): String {
        return Locale("", countryCode).displayCountry
    }
}
