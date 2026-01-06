package com.example.placy

import kotlinx.serialization.Serializable

@Serializable
data class GeoMark(
    val photoUUID: String,
    val latitude: Double,
    val longitude: Double
)