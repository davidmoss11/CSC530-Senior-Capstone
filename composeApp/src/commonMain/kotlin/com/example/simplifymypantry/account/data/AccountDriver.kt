package com.example.simplifymypantry.account.data

import app.cash.sqldelight.db.SqlDriver

expect class AccountDriver{
    fun createDriver(): SqlDriver
}

fun createAccountDatabase(driverFactory: AccountDriver) : AccountDatabase {
    val driver = driverFactory.createDriver()
    return AccountDatabase(driver)
}