package com.example.simplifymypantry.pantry.data

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): PantryDatabase {
    val driver = driverFactory.createDriver()
    return PantryDatabase(driver)
}
