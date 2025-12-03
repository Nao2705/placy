package com.example.placy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import kotlinx.coroutines.launch

@Composable
fun CameraView() {
    var showCamera by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val nextcloudClient = remember {
        NextcloudClient(
            baseUrl = BuildConfig.SERVER_URL,
            pass = BuildConfig.PASSWORD,
            user = BuildConfig.USERNAME
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (showCamera) {
            ImagePickerLauncher(
                config = ImagePickerConfig(
                    onPhotoCaptured = { result ->
                        showCamera = false
                        scope.launch {
                            nextcloudClient.uploadImage(result.loadBytes())
                        }
                    },
                    onError = {
                        showCamera = false
                    },
                    onDismiss = {
                        showCamera = false
                    }
                )
            )
        }
    }

    Button(onClick = { showCamera = true }) {
        Text("Take Photo")
    }
}