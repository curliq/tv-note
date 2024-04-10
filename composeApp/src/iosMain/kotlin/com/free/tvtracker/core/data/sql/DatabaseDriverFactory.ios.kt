package com.free.tvtracker.core.data.sql

import co.touchlab.sqliter.DatabaseConfiguration
import com.free.tvtracker.shared.db.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, "tracked.db", onConfiguration = { config: DatabaseConfiguration ->
            config.copy(
                extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true)
            )
        })
    }
}
