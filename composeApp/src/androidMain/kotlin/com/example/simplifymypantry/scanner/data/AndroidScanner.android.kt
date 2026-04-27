package com.example.simplifymypantry.scanner.data

import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi

class AndroidScanner(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) : Scanner {
    private var cameraProvider: ProcessCameraProvider? = null
    private val _scannedCodes = MutableSharedFlow<String>(replay = 1)
    override val scannedCodes = _scannedCodes.asSharedFlow()

    private val scanner = BarcodeScanning.getClient()

    val previewView: PreviewView = PreviewView(context)

    var isScanning = false

    @OptIn(ExperimentalGetImage::class)
    override fun startScanning() {
        if (isScanning) return
        isScanning = true

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({

            cameraProvider = cameraProviderFuture.get()

            val cameraProvider = this.cameraProvider ?: return@addListener

            cameraProvider.unbindAll()

            val analysis = ImageAnalysis.Builder().build()

            analysis.setAnalyzer(
                ContextCompat.getMainExecutor(context)
            ) { imageProxy ->
                Log.d("AndroidScanner", "imageProxy Successful")
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    Log.d("AndroidScanner", "mediaImage Detected")
                    val image = InputImage.fromMediaImage(
                        mediaImage,
                        imageProxy.imageInfo.rotationDegrees
                    )

                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            Log.d("AndroidScanner", "Barcodes found: ${barcodes.size}")
                            barcodes.firstOrNull()?.rawValue?.let { code ->
                                Log.d("AndroidScanner", "Code value: $code")
                                _scannedCodes.tryEmit(code)
                            } ?: Log.d("AndroidScanner", "No barcode value extracted")
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                }
            }

            val preview = Preview.Builder().build()

            preview.setSurfaceProvider(previewView.surfaceProvider)

            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analysis
            )

        }, ContextCompat.getMainExecutor(context))
    }

    override fun stopScanning() {

        cameraProvider?.unbindAll()
        isScanning = false
    }

    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    override fun clearLastCode() {
        _scannedCodes.resetReplayCache()
    }


    override fun getCameraPreview(): Any = previewView
}