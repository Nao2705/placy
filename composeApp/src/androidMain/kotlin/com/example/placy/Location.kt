package com.example.placy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.launch

@Composable
fun LocationTest() {
    val scope = rememberCoroutineScope()
    var result by remember { mutableStateOf<GeolocatorResult?>(null) }
    val geolocator = Geolocator.mobile()
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            scope.launch {
                result = geolocator.current(Priority.HighAccuracy)
            }
        }) {
            Text("Get location")
        }
        result?.let {
            when (it) {
                is GeolocatorResult.Success -> {
                    Text("Current location: ${it.getOrNull()!!.coordinates.longitude};${it.getOrNull()!!.coordinates.latitude}") }

                else -> {}
            }
        }
    }
}