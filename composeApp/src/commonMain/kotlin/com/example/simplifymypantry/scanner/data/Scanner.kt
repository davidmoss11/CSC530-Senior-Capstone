package com.example.simplifymypantry.scanner.data

import kotlinx.coroutines.flow.Flow

interface Scanner {
    val scannedCodes: Flow<String>
    fun startScanning()
    fun stopScanning()

    fun clearLastCode()

    fun getCameraPreview(): Any? = null
}