package com.zerozero.store.infrastructure.kakao.search.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class KakaoSearchResponseWrapper(
    @JsonProperty("meta")
    val kakaoSearchMeta: KakaoSearchMeta,

    @JsonProperty("documents")
    val kakaoSearchResponses: List<KakaoSearchResponse>,
)

data class KakaoSearchMeta(
    @JsonProperty("total_count")
    val totalCount: Int,

    @JsonProperty("pageable_count")
    val pageableCount: Int,

    @JsonProperty("is_end")
    val isEnd: Boolean,

    @JsonProperty("same_name")
    val sameName: SameName,
)

data class SameName(
    val region: List<String>,
    val keyword: String,

    @JsonProperty("selected_region")
    val selectedRegion: String,
)

data class KakaoSearchResponse(
    val id: String,

    @JsonProperty("place_name")
    val placeName: String,

    @JsonProperty("category_name")
    val categoryName: String,

    @JsonProperty("category_group_code")
    val categoryGroupCode: String,

    @JsonProperty("category_group_name")
    val categoryGroupName: String,

    val phone: String,

    @JsonProperty("address_name")
    val addressName: String,

    @JsonProperty("road_address_name")
    val roadAddressName: String,

    val x: String,
    val y: String,

    @JsonProperty("place_url")
    val placeUrl: String,

    val distance: String,
    val status: Boolean?,
    val storeId: UUID?,
)
