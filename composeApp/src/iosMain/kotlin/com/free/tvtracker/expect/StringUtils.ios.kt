package com.free.tvtracker.expect

import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterStyle
import kotlin.math.pow

actual open class StringUtils actual constructor() {
    actual fun roundDouble(d: Double, decimalPoints: Int): String {
        var multiplier = 1.0
        repeat(decimalPoints) { multiplier *= 10 }
        val r = kotlin.math.round(d * multiplier) / multiplier
        return r.toString()
    }

    actual fun shortenDouble(d: Double, decimalPoints: Int, shortenCharacter: Char): String {
        require(decimalPoints >= 0) { "Decimal points cannot be negative" }

        val factor = 10.0.pow(decimalPoints)
        val rounded = kotlin.math.round(d * factor) / factor

        val numberStr = rounded.toString()
        val parts = numberStr.split('.')

        val wholePart = parts[0]
        val decimalPart = when {
            parts.size > 1 -> {
                val decimal = parts[1].padEnd(decimalPoints, '0').take(decimalPoints)
                if (decimal.isNotEmpty()) ".$decimal" else ""
            }

            decimalPoints > 0 -> "." + "0".repeat(decimalPoints)
            else -> ""
        }

        return wholePart + decimalPart + shortenCharacter
    }

    actual fun formatMoney(d: Double, language: String, countryCode: String): String {
        val locale = NSLocale(localeIdentifier = "${language}_$countryCode")
        val formatter = NSNumberFormatter().apply {
            numberStyle = 2u // Currency style: 2 represents currency
            this.locale = locale
            minimumFractionDigits = 2u
            maximumFractionDigits = 2u
        }

        fun Double.round(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return kotlin.math.round(this * multiplier) / multiplier
        }

        return formatter.stringFromNumber(NSNumber(d)) ?: run {
            // Fallback formatting
            val rounded = d.round(2)
            when (countryCode) {
                "US" -> "$$rounded"
                "GB" -> "£$rounded"
                "EU" -> "$rounded€"
                else -> "$countryCode $rounded"
            }
        }
    }
}
