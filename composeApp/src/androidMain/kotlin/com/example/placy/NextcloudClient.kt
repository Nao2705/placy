package com.example.placy

import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.statement.readRawBytes
import io.ktor.http.*

class NextcloudClient(
    private val baseUrl: String,
    private val user: String,
    private val pass: String
) {
    private val client = HttpClient {
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(user, pass)
                }
                sendWithoutRequest { true }
            }
        }
    }

    suspend fun uploadImage(imageData: ByteArray): Boolean {
        val url = "$baseUrl/remote.php/dav/files/$user/test.jpg"

        try {
            val response: HttpResponse = client.put(url) {
                setBody(imageData)
                contentType(ContentType.Image.JPEG)
            }

            return response.status.isSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    suspend fun downloadImage(remotePath: String): ByteArray? {
        val cleanPath = remotePath.trimStart('/')
        val url = "$baseUrl/remote.php/dav/files/$user/$cleanPath"

        return try {
            val response: HttpResponse = client.get(url)

            if (response.status.isSuccess()) {
                response.readRawBytes()
            } else {
                println("Download failed: ${response.status}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun close() {
        client.close()
    }
}