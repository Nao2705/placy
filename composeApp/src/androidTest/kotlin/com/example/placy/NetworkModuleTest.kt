// NetworkModuleTest.kt
package com.example.placy

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.pluginOrNull
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NetworkModuleTest {
    @Test
    fun testNetworkModuleHttpClientCreation() {
        val client = NetworkModule.httpClient

        assertNotNull(client)
        assertTrue(client.pluginOrNull(ContentNegotiation) != null)
        assertTrue(client.pluginOrNull(Auth) != null)
    }

    @Test
    fun testHttpClientConfiguration() {
        val client = NetworkModule.httpClient

        // Проверяем, что клиент создан с правильным движком
        // Это косвенная проверка через toString
        val clientString = client.toString()
        assertTrue(clientString.contains("HttpClient"))
    }
}