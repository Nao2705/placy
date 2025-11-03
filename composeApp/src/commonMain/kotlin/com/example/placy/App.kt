package com.example.placy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.map.CameraPosition


@Composable
@Preview
fun App() {
    MaterialTheme {
        MapScreen()
    }
}

@Composable
fun MapScreen() {
    val cameraPositionState = rememberCameraPositionState { position =
        CameraPosition(
            Point(55.751225, 37.62954),
            17.0f,
            150.0f,
            30.0f
        )
    }
    YandexMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize()
    )
}