package com.example.placy

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.rotationMatrix
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.launch
import io.ktor.websocket.Frame
import kotlinx.datetime.Month
import org.jetbrains.compose.ui.tooling.preview.Preview
import placy.composeapp.generated.resources.Res
import placy.composeapp.generated.resources.*
import ru.sulgik.mapkit.PointF
import ru.sulgik.mapkit.compose.Placemark
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.imageProvider
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.rememberPlacemarkState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.map.IconStyle
import ru.sulgik.mapkit.map.RotationType

@Composable
@Preview
fun TempApp() {
    MaterialTheme {
        TempMapScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempMapScreen() {
    val navController = rememberNavController()
    var marks by remember { mutableStateOf<List<GeoMark>>(emptyList()) }
    val context = LocalPlatformContext.current
    var result by remember { mutableStateOf<GeolocatorResult?>(null) }
    val apiService = remember { ApiService(NetworkModule.httpClient) }
    var selectedMark by remember { mutableStateOf<GeoMark?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val imageLoader = remember {
        getNextcloudImageLoader(context)
    }
    val cameraPositionState = rememberCameraPositionState()
    val geolocator = Geolocator.mobile()

    LaunchedEffect(Unit) {
        marks = apiService.getAllMarks()
    }

    // Навигационный граф
    NavHost(
        navController = navController,
        startDestination = "map"
    ) {
        composable("map") {
            // Основной экран с картой и кнопкой
            Box(modifier = Modifier.fillMaxSize()) {
                // Карта
                YandexMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    marks.forEach { mark ->
                        Placemark(
                            state = rememberPlacemarkState(
                                Point(mark.latitude, mark.longitude),
                                direction = 25f
                            ),
                            icon = imageProvider(Res.drawable.red_pin),
                            iconStyle = IconStyle(
                                anchor = PointF(0.5f, 1f),
                                rotationType = RotationType.ROTATE,
                                scale = when {
                                    cameraPositionState.position.zoom < 8f -> 0.3f
                                    cameraPositionState.position.zoom < 13f -> 0.4f
                                    else -> 0.5f
                                },
                                zIndex = 1f
                            ),
                            onTap = {
                                selectedMark = mark
                                showSheet = true
                                true
                            }
                        )
                    }
                }

                if (showSheet && selectedMark != null) {
                    Log.d("test", "test")
                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                        sheetState = sheetState
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            val imageUrl = "${BuildConfig.SERVER_URL}/remote.php/dav/files/${BuildConfig.USERNAME}/${selectedMark!!.photoUUID}.jpg"

                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Photo",
                                imageLoader = imageLoader,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                // Кнопка камеры
                @OptIn(ExperimentalMaterial3Api::class)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding() + 16.dp
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        onClick = {
                            scope.launch {
                                result = geolocator.current(Priority.HighAccuracy)
                            }
                            result?.let {
                                when (it) {
                                    is GeolocatorResult.Success -> {
                                        navController.navigate("camera")
                                    }
                                    else -> {}
                                }
                            }
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_camera_playstore),
                            contentDescription = "Камера",
                            modifier = Modifier.size(72.dp),
                            // Дополнительные настройки для Image
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }

        composable("camera") {
            // Экран камеры
            CameraView(
                navController = navController, result!!.getOrNull()!!.coordinates.latitude,
                result!!.getOrNull()!!.coordinates.longitude)
        }
    }
}
