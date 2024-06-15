package com.free.tvtracker.expect

class CommonStringUtils : StringUtils() {
    fun listToString(list: List<String>): String {
        return when {
            list.isEmpty() -> ""
            list.size == 1 -> list[0]
            list.size == 2 -> "${list[0]} and ${list[1]}"
            else -> {
                val allButLast = list.dropLast(1).joinToString(", ")
                "$allButLast, and ${list.last()}"
            }
        }
    }
}

expect open class StringUtils() {
    fun roundDouble(d: Double, decimalPoints: Int): String

    fun shortenDouble(d: Double, decimalPoints: Int, shortenCharacter: Char): String
}
