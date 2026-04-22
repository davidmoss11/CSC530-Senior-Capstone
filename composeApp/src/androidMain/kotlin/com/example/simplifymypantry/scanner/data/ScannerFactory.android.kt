package com.example.simplifymypantry.scanner.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class ScannerDriver(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(PantryItemCache.Schema, context, "pantryItemCache.db")
    }
}
