package com.free.tvtracker.expect

import java.text.NumberFormat
import java.util.Locale

actual open class StringUtils actual constructor() {
    actual fun roundDouble(d: Double, decimalPoints: Int): String {
        return "%.${decimalPoints}f".format(d)
    }

    actual fun shortenDouble(
        d: Double,
        decimalPoints: Int,
        shortenCharacter: Char
    ): String {
        return "%.${decimalPoints}f%c".format(d, shortenCharacter)
    }

    actual fun formatMoney(d: Double, language: String, countryCode: String): String {
        return NumberFormat.getCurrencyInstance(Locale(language, countryCode)).format(d)
    }
}
