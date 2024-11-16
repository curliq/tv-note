package com.free.tvtracker.expect

actual open class StringUtils actual constructor() {
    actual fun roundDouble(d: Double, decimalPoints: Int): String {
        return "" //todo
    }

    actual fun shortenDouble(
        d: Double,
        decimalPoints: Int,
        shortenCharacter: Char
    ): String {
        return ""//todo
    }

    actual fun formatMoney(d: Double, language: String, countryCode: String): String {
        return "" //todo
    }
}
