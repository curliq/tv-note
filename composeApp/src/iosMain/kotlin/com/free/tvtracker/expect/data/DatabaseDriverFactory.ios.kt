package com.free.tvtracker.expect.data

import co.touchlab.sqliter.DatabaseConfiguration
import co.touchlab.sqliter.DatabaseConfiguration.Extended
import com.free.tvtracker.shared.db.AppDatabase.Companion.Schema
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection

actual class DatabaseDriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {

        val dbConfig = DatabaseConfiguration(
            name = "tracked.db",
            version = 1,
            extendedConfig = Extended(foreignKeyConstraints = true),
            create = { connection ->
                wrapConnection(connection) { Schema.create(it) }
            },
//            upgrade = { connection, oldVersion, newVersion ->
//                wrapConnection(connection) { Schema.migrate(it, oldVersion, newVersion) }
//            }
        )
        return NativeSqliteDriver(dbConfig)
    }
}
