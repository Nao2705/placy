package com.example.placy

import coil3.*
import coil3.network.ktor3.KtorNetworkFetcherFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic

fun getNextcloudImageLoader(
    context: PlatformContext,
    baseUrl: String,
    username: String,
    password: String
): ImageLoader {

    val authClient = HttpClient(CIO) {
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(username, password)
                }
                sendWithoutRequest { request ->
                    request.url.toString().startsWith(baseUrl) // ?
                }
            }
        }
    }

    return ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory(authClient))
        }
        .build()
}