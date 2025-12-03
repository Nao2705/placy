package com.example.placy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import placy.composeapp.generated.resources.Res
import placy.composeapp.generated.resources.pin
import ru.sulgik.mapkit.compose.Placemark
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.imageProvider
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.rememberPlacemarkState
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
    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition(
                Point(55.751225, 37.62954),
                17.0f,
                150.0f,
                30.0f
            )
    }
    val imageProvider = imageProvider(Res.drawable.pin)
    YandexMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize()
    )
    {
        Placemark(
            state = rememberPlacemarkState(Point(55.751225, 37.62954)),
            icon = imageProvider
        )
    }
}