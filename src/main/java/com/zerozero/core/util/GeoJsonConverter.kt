package com.zerozero.core.util

import org.springframework.data.mongodb.core.geo.GeoJsonPoint

object GeoJsonConverter {
    fun of(longitude: String, latitude: String): GeoJsonPoint = GeoJsonPoint(longitude.toDouble(), latitude.toDouble())
}
