package com.example.simplifymypantry.scanner.presentation

import androidx.compose.runtime.Composable
import com.example.simplifymypantry.scanner.data.Scanner

@Composable
expect fun CameraPreviewView(scanner: Scanner)

@Composable
expect fun RequestCameraPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit
)