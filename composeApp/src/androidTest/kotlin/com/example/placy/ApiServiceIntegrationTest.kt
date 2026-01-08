package com.example.placy

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApiServiceIntegrationTest {

    @Test
    fun testApiServiceInitialization() {
        val mockClient = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond("[]", HttpStatusCode.OK)
                }
            }
        }

        val apiService = ApiService(mockClient)

        assertTrue(apiService != null)
    }

    @Test
    fun testApiServiceBaseUrl() = runTest {
        var capturedUrl = ""
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    capturedUrl = request.url.toString()
                    respond(
                        content = "[]",
                        status = HttpStatusCode.OK
                    )
                }
            }
        }

        val apiService = ApiService(client)
        apiService.getAllMarks()

        assertTrue(capturedUrl.startsWith("http://188.242.76.219:57492"))
        assertTrue(capturedUrl.endsWith("/geomarks"))
    }

    @Test
    fun testSaveMarkRequestBody() = runTest {
        var capturedBody = ""
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    capturedBody = request.body.toByteArray().decodeToString()
                    respond("", HttpStatusCode.Created)
                }
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val apiService = ApiService(client)
        val mark = GeoMark("test-id", 12.345, 67.890)

        val result = apiService.saveMark(mark)

        assertTrue(result)
        assertTrue(capturedBody.contains("test-id"))
        assertTrue(capturedBody.contains("12.345"))
        assertTrue(capturedBody.contains("67.89"))
    }
}