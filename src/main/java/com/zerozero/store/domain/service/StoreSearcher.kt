package com.zerozero.store.domain.service

import com.zerozero.store.domain.response.StoreSearchResponse

interface StoreSearcher {
    fun search(query: String): List<StoreSearchResponse>

    fun searchByLocation(query: String, longitude: Double, latitude: Double): List<StoreSearchResponse>
}
