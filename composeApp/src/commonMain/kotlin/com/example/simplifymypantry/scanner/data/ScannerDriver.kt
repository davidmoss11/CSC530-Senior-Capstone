package com.example.simplifymypantry.scanner.data

import app.cash.sqldelight.db.SqlDriver

expect class ScannerDriver{
    fun createDriver(): SqlDriver
}

fun createScannerDatabase(driverFactory: ScannerDriver) : PantryItemCache {
    val driver = driverFactory.createDriver()
    return PantryItemCache(driver)
}