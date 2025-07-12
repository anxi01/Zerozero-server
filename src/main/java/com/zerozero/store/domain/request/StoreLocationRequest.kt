package com.zerozero.store.domain.request

@JvmRecord
data class StoreLocationRequest(
    val longitude: Double,
    val latitude: Double,
    val accessToken: String
)
