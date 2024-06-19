package com.free.tvtracker.expect.data

import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import com.free.tvtracker.AndroidApplication
import com.free.tvtracker.shared.db.AppDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.logs.LogSqliteDriver

const val DatabaseNameAndroid = "tracked.db"

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return LogSqliteDriver(AndroidSqliteDriver(
            AppDatabase.Schema,
            AndroidApplication.context,
            DatabaseNameAndroid,
            callback = object : AndroidSqliteDriver.Callback(AppDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            })
        ) {
            Log.d("sqldelight", it)
        }
    }
}
