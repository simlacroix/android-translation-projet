package com.example.translationproject.CameraPage

import CameraPageViewModel
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.translationproject.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun CameraPage(onBackPressed: () -> Unit, viewModel: CameraPageViewModel = koinViewModel()) {
    val context = LocalContext.current
    val previewView: PreviewView = remember { PreviewView(context) }
    val cameraController = remember { LifecycleCameraController(context) }
    val lifecycleOwner = LocalLifecycleOwner.current
    cameraController.bindToLifecycle(lifecycleOwner)
    cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    previewView.controller = cameraController

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(100.dp),
            onClick = { viewModel.takePhoto(cameraController) }) {
            Icon(
                painter = painterResource(R.drawable.camera_button),
                contentDescription = "Take photo",
                tint = Color.White,
                modifier = Modifier.padding(bottom = 30.dp)
            )
        }
        if (viewModel.panelIsOpen) {
            CameraTextPanel(
                onConfirmRequest = { x ->
                    viewModel.storeCameraText(x)
                    viewModel.panelIsOpen = false
                    onBackPressed()
                },
                currentlyDetectedText = viewModel.currentlyDetectedText,
                onDismissRequest = { viewModel.panelIsOpen = false })
        }
        if (viewModel.errorPanelIsOpen) {
            ErrorPanel {
                viewModel.errorPanelIsOpen = false
            }
        }
    }
}

@Composable
fun CameraTextPanel(
    currentlyDetectedText: String,
    onConfirmRequest: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmRequest(currentlyDetectedText) }) {
                Text(text = LocalContext.current.getString(R.string.yes))
            }
        },
        title = { Text(text = LocalContext.current.getString(R.string.detected_text)) },
        text = {
            Column {
                Text(text = currentlyDetectedText, color = Color.Red)
                Text(text = LocalContext.current.getString(R.string.ask_translate))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = LocalContext.current.getString(R.string.no))
            }
        })
}

@Composable
fun ErrorPanel(onDismissRequest: () -> Unit) {
    AlertDialog(
        title = { Text(text = LocalContext.current.getString(R.string.scan_error)) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = LocalContext.current.getString(R.string.ok))
            }
        })
}