package com.example.placy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext

@Composable
fun NextcloudScreen() {
    val context = LocalPlatformContext.current
    val baseUrl = BuildConfig.SERVER_URL
    val username = BuildConfig.USERNAME
    val password = BuildConfig.PASSWORD

    val imageLoader = remember {
        getNextcloudImageLoader(context, baseUrl, username, password)
    }

    val imagePath = "Nextcloud.png"
    val fullUrl = "$baseUrl/remote.php/dav/files/$username/$imagePath"


    AsyncImage(
        model = fullUrl,
        contentDescription = "My Photo from Nextcloud",
        imageLoader = imageLoader,
        modifier = Modifier.fillMaxSize()
    )

}