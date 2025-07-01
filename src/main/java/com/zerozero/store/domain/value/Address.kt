package com.zerozero.store.domain.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Address(
    @Column(name = "address", nullable = false)
    val address: String,

    @Column(name = "road_address", nullable = false)
    val roadAddress: String
)
