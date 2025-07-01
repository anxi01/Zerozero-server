package com.zerozero.store.domain.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class GeoLocation(
    @Column(name = "longitude", nullable = false)
    val longitude: String,

    @Column(name = "latitude", nullable = false)
    val latitude: String
)

