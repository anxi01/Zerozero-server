package com.zerozero.core.util

import org.springframework.data.mongodb.core.geo.GeoJsonPoint

object GeoJsonConverter {
    @JvmStatic
    fun of(longitude: String, latitude: String): GeoJsonPoint? =
        runCatching {
            GeoJsonPoint(longitude.toDouble(), latitude.toDouble())
        }.getOrNull()
}
