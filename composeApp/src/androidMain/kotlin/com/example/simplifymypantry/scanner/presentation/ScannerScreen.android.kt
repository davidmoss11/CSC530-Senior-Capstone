package com.example.simplifymypantry.scanner.presentation

import android.Manifest
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.simplifymypantry.scanner.data.Scanner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
actual fun CameraPreviewView(scanner: Scanner) {
    val previewView = scanner.getCameraPreview()
    if (previewView is PreviewView) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun RequestCameraPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    // 2. React to result
    LaunchedEffect(permissionState.status) {
        when {
            permissionState.status.isGranted -> onGranted()

            permissionState.status.shouldShowRationale -> {
                // user denied once, but can ask again
                onDenied()
            }

            else -> {
                // permanently denied or initial idle state
                onDenied()
            }
        }
    }
}