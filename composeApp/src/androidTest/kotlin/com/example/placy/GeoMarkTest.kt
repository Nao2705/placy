package com.example.placy

import junit.framework.TestCase.assertTrue
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class GeoMarkTest {
    @Test
    fun testGeoMarkProperties() {
        val mark = GeoMark("test-uuid", 55.751244, 37.618423)

        assertEquals("test-uuid", mark.photoUUID)
        assertEquals(55.751244, mark.latitude, 0.000001)
        assertEquals(37.618423, mark.longitude, 0.000001)
    }

    @Test
    fun testGeoMarkSerialization() {
        val mark = GeoMark("serialize-test", 12.34, 56.78)
        val json = Json.encodeToString(mark)

        assertTrue(json.contains("serialize-test"))
        assertTrue(json.contains("12.34"))
        assertTrue(json.contains("56.78"))
    }

    @Test
    fun testGeoMarkDeserialization() {
        val json = """{"photoUUID":"deserialize-test","latitude":90.0,"longitude":180.0}"""
        val mark = Json.decodeFromString<GeoMark>(json)

        assertEquals("deserialize-test", mark.photoUUID)
        assertEquals(90.0, mark.latitude, 0.001)
        assertEquals(180.0, mark.longitude, 0.001)
    }
}