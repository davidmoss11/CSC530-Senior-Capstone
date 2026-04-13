package com.example.simplifymypantry

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.simplifymypantry.app.App
import com.example.simplifymypantry.pantry.data.DriverFactory

fun main() = application {
    val driverFactory = remember { DriverFactory() }
    Window(
        onCloseRequest = ::exitApplication,
        title = "SimplifyMyPantry",
    ) {
        App(driverFactory = driverFactory)
    }
}
