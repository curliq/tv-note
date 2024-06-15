package com.free.tvtracker.expect

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
}
