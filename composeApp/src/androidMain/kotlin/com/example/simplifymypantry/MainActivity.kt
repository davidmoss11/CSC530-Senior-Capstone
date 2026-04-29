package com.example.simplifymypantry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.simplifymypantry.account.data.AccountDriver
import com.example.simplifymypantry.account.data.createAccountDatabase
import com.example.simplifymypantry.app.App
import com.example.simplifymypantry.pantry.data.DriverFactory
import com.example.simplifymypantry.pantry.data.createDatabase
import com.example.simplifymypantry.scanner.data.AndroidImageSaver
import com.example.simplifymypantry.scanner.data.AndroidScanner
import com.example.simplifymypantry.scanner.data.ScannerDriver
import com.example.simplifymypantry.scanner.data.createScannerDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            val driverFactory = DriverFactory(applicationContext)
            val accountDriver = AccountDriver(applicationContext)
            val scannerDriver = ScannerDriver(applicationContext)
            val scanner = AndroidScanner(applicationContext, this@MainActivity )
            val imageSaver = AndroidImageSaver(applicationContext)

            val database = createDatabase(driverFactory)
            val accountDatabase = createAccountDatabase(accountDriver)
            val scannerDatabase = createScannerDatabase(scannerDriver)

            setContent {
                App(database, accountDatabase, scannerDatabase, scanner, imageSaver)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
