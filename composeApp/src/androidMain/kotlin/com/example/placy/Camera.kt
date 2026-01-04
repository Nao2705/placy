package com.example.placy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CameraView(navController: NavController) {
    var cameraState by remember { mutableStateOf<CameraState>(CameraState.Loading) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val nextcloudClient = remember {
        NextcloudClient(
            baseUrl = BuildConfig.SERVER_URL,
            pass = BuildConfig.PASSWORD,
            user = BuildConfig.USERNAME
        )
    }

    LaunchedEffect(Unit) {
        // Небольшая задержка перед открытием камеры
        delay(300)
        cameraState = CameraState.Ready
    }

    when (cameraState) {
        CameraState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
                Text("Подготовка камеры...")
            }
        }

        CameraState.Ready -> {
            ImagePickerLauncher(
                config = ImagePickerConfig(
                    onPhotoCaptured = { result ->
                        cameraState = CameraState.Uploading
                        scope.launch {
                            try {
                                val bytes = result.loadBytes()
                                val success = nextcloudClient.uploadImage(bytes)
                                if (success) {
                                    // Успешно загружено
                                    navController.popBackStack()
                                } else {
                                    // Ошибка загрузки
                                    cameraState = CameraState.Error("Ошибка загрузки")
                                }
                            } catch (e: Exception) {
                                cameraState = CameraState.Error("Ошибка: ${e.message}")
                            }
                        }
                    },
                    onError = { error ->
                        navController.popBackStack() // Возвращаемся при ошибке камеры
                    },
                    onDismiss = {
                        navController.popBackStack() // Возвращаемся при отмене
                    }
                )
            )

            // Показываем индикатор загрузки во время загрузки фото
            if (cameraState == CameraState.Uploading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                    Text("Загрузка фото...")
                }
            }
        }

        is CameraState.Error -> {
            val errorMessage = (cameraState as CameraState.Error).message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Ошибка: $errorMessage")
            }
        }

        CameraState.Uploading -> {
            // Уже обрабатывается выше
        }
    }
}

sealed class CameraState {
    object Loading : CameraState()
    object Ready : CameraState()
    object Uploading : CameraState()
    data class Error(val message: String) : CameraState()
}