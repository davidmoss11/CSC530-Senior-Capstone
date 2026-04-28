package com.example.simplifymypantry.scanner.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.simplifymypantry.scanner.data.Scanner
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.requestAccessForMediaType

@Composable
actual fun CameraPreviewView(scanner: Scanner) { /* no-op */ }

@Composable
actual fun RequestCameraPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {
    LaunchedEffect(Unit) {
        AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
            if (granted) onGranted() else onDenied()
        }
    }
}