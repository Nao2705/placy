package com.example.placy

import coil3.*
import coil3.network.ktor3.KtorNetworkFetcherFactory

fun getNextcloudImageLoader(
    context: PlatformContext,
): ImageLoader {

    return ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory(NetworkModule.httpClient))
        }
        .build()
}