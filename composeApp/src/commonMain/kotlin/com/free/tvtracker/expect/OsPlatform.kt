@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.free.tvtracker.expect

expect class OsPlatform() {
    enum class Platform { Android, IOS, Other }

    fun get(): Platform
}
