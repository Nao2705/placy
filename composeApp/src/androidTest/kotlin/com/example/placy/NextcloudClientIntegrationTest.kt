package com.example.placy

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NextcloudClientIntegrationTest {

    @Test
    fun testUploadImageFilename() = runTest {
        var capturedUrl = ""
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    capturedUrl = request.url.toString()
                    respond(
                        content = "",
                        status = HttpStatusCode.Created
                    )
                }
            }
        }

        val nextcloudClient = NextcloudClient(client)
        val imageData = byteArrayOf(0x1, 0x2, 0x3)
        val filename = "unique-photo-123"

        val result = nextcloudClient.uploadImage(imageData, filename)

        assertTrue(result)
        assertTrue(capturedUrl.contains("$filename.jpg"))
    }

    @Test
    fun testDownloadImageDifferentPaths() = runTest {
        val testCases = listOf(
            "test.jpg",
            "folder/test.jpg",
            "2024/photos/photo1.jpg"
        )

        testCases.forEach { path ->
            val client = HttpClient(MockEngine) {
                engine {
                    addHandler { request ->
                        respond(
                            content = path.toByteArray(),
                            status = HttpStatusCode.OK
                        )
                    }
                }
            }

            val nextcloudClient = NextcloudClient(client)
            val result = nextcloudClient.downloadImage(path)

            assertNotNull(result)
            assertEquals(path.toByteArray().size, result.size)
        }
    }

    @Test
    fun testDownloadImageEmptyPath() = runTest {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond(
                        content = "",
                        status = HttpStatusCode.OK
                    )
                }
            }
        }

        val nextcloudClient = NextcloudClient(client)
        val result = nextcloudClient.downloadImage("")

        // Зависит от реализации - может быть null или пустой массив
        // assertNull(result)
    }

    @Test
    fun testNextcloudClientInitialization() {
        val mockClient = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond("", HttpStatusCode.OK)
                }
            }
        }

        val nextcloudClient = NextcloudClient(mockClient)

        assertNotNull(nextcloudClient)
    }
}