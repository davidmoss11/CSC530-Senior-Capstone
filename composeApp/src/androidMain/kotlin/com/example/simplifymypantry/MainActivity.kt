package com.example.simplifymypantry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.simplifymypantry.app.App
import com.example.simplifymypantry.pantry.data.DriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val driverFactory = remember { DriverFactory(applicationContext) }
            App(driverFactory = driverFactory)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
}
