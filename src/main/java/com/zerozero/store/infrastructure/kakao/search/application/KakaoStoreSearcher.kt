package com.zerozero.store.infrastructure.kakao.search.application

import com.zerozero.store.domain.response.StoreSearchResponse
import com.zerozero.store.domain.service.StoreSearcher
import com.zerozero.store.exception.StoreErrorType
import com.zerozero.store.exception.StoreException
import com.zerozero.store.infrastructure.kakao.search.request.KakaoSearchRequest
import com.zerozero.store.infrastructure.kakao.search.response.KakaoSearchResponseWrapper
import lombok.RequiredArgsConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@RequiredArgsConstructor
class KakaoStoreSearcher(
    private val kakaoSearchClient: KakaoSearchClient,
) : StoreSearcher {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun search(query: String): List<StoreSearchResponse> {
        val request = KakaoSearchRequest(
            query = query
        )
        return executeSearch(request)
    }

    override fun searchByLocation(query: String, longitude: Double, latitude: Double): List<StoreSearchResponse> {
        val request = KakaoSearchRequest(
            query = query,
            x = longitude.toString(),
            y = latitude.toString(),
            radius = DEFAULT_RADIUS
        )
        return executeSearch(request)
    }

    private fun executeSearch(request: KakaoSearchRequest): List<StoreSearchResponse> {
        val response: KakaoSearchResponseWrapper = kakaoSearchClient.search(request)

        if (response.kakaoSearchMeta.totalCount == 0) {
            log.error("[KakaoStoreSearcher] Kakao search response is null or empty")
            throw StoreException(StoreErrorType.NOT_EXIST_SEARCH_RESPONSE)
        }

        return response.kakaoSearchResponses.map(StoreSearchResponse::from)
    }

    companion object {
        private const val DEFAULT_RADIUS = 2000
    }
}
