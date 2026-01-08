package com.example.placy

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CameraStateTest {
    @Test
    fun testCameraStateSealedClass() {
        val loading = CameraState.Loading
        val ready = CameraState.Ready
        val uploading = CameraState.Uploading
        val error = CameraState.Error("Test error")

        assertTrue(loading is CameraState)
        assertTrue(ready is CameraState)
        assertTrue(uploading is CameraState)
        assertTrue(error is CameraState)
    }

    @Test
    fun testCameraStateErrorMessage() {
        val errorMessage = "Network error"
        val errorState = CameraState.Error(errorMessage)

        assertEquals(errorMessage, errorState.message)
    }
}