package com.example.simplifymypantry.scanner.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.simplifymypantry.pantry.presentation.PantryItemDialog
import org.jetbrains.compose.resources.painterResource
import simplifymypantry.composeapp.generated.resources.Res
import simplifymypantry.composeapp.generated.resources.close_24px


@Composable
fun ScannerScreen(viewModel: ScannerScreenViewModel, returnHome: () -> Unit){

    var permissionGranted by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }

    RequestCameraPermission(
        onGranted = { permissionGranted = true },
        onDenied = { permissionDenied = true }
    )

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) viewModel.start()
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        when {
            permissionDenied -> {
                Text("Camera permission is required to scan barcodes.")
            }
            permissionGranted -> {
                CameraPreviewView(viewModel.scanner)
                Text(
                    text =
                        if (!viewModel.isLoading) "Scanning..."
                        else "Loading Result...",
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            else -> {
                Text("Requesting camera permission...")
            }
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 20.dp,
                    end = 10.dp,
                    start = 0.dp,
                    bottom = 0.dp
                )
                .size(40.dp),
            onClick = {
                viewModel.stop()
                returnHome()
                      },
            colors = IconButtonColors(
                containerColor = MaterialTheme.colorScheme.onTertiary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Icon(
                painter = painterResource(Res.drawable.close_24px),
                contentDescription = "Close"
            )
        }

        if (viewModel.popupDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDialog() },
                title = { Text(
                    "Barcode Found",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondary
                    ) },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Is this your item?",
                            color = MaterialTheme.colorScheme.onSecondary
                            )
                        Spacer(Modifier.height(8.dp))

                        AsyncImage(
                            model =  ImageRequest.Builder(LocalPlatformContext.current)
                                .data(viewModel.displayImagePath)
                                .crossfade(true)
                                .build(),
                            onState = { state ->
                                if (state is AsyncImagePainter.State.Error) {
                                    viewModel.imageError = "Image failed to load"
                                } else if (state is AsyncImagePainter.State.Success) {
                                    viewModel.imageError = null
                                }
                            },
                            contentDescription = "Product image",
                            modifier = Modifier
                                .size(400.dp)
                        )
                        if (viewModel.imageError != null) {
                            Text(
                                text = viewModel.imageError!!,
                                color = Color.Red
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = viewModel.cache?.productName ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.confirmDialog() }) {
                        Text(
                            "Yes",
                            color = MaterialTheme.colorScheme.onSecondary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissDialog() }) {
                        Text(
                            "Scan Again",
                            color = MaterialTheme.colorScheme.onSecondary)
                    }
                }
            )
        }

        if (viewModel.saveDialog){
            viewModel.pendingPantryItem?.let { item ->
                PantryItemDialog(
                    title = "Add to Pantry?",
                    initialItem = item,
                    onDismiss = { viewModel.dismissSaveItem() },
                    onConfirm = { updatedItem -> viewModel.confirmSaveItem(updatedItem) }
                )
            }
        }

    }
}