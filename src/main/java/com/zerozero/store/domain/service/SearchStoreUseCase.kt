package com.zerozero.store.domain.service

import com.zerozero.store.domain.repository.StoreRepository
import com.zerozero.store.domain.response.StoreResponse
import com.zerozero.store.domain.value.GeoLocation
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SearchStoreUseCase(
    private val storeRepository: StoreRepository,
    private val storeSearcher: StoreSearcher,
) {

    fun execute(query: String): List<StoreResponse> {
        val storeSearchResponses = storeSearcher.search(query)

        return storeSearchResponses.map { storeSearchResponse ->
            val store = storeRepository.findByNameAndGeoLocation(
                storeSearchResponse.placeName,
                GeoLocation(storeSearchResponse.longitude, storeSearchResponse.latitude)
            )

            if (store == null) {
                StoreResponse.of(storeSearchResponse, null, false)
            } else {
                StoreResponse.of(storeSearchResponse, store.id, store.status)
            }
        }
    }
}
