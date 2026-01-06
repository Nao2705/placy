package com.example.placy

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object NetworkModule {
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }

        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(BuildConfig.USERNAME, BuildConfig.PASSWORD)
                }
                sendWithoutRequest { request ->
                    request.url.toString().contains(BuildConfig.SERVER_URL)
                }
            }
        }
    }
}