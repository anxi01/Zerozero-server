package com.zerozero.store.infrastructure.kakao.search.request

import com.zerozero.store.infrastructure.kakao.search.core.CategoryGroupCode

data class KakaoSearchRequest(
    val query: String,
    val categoryGroupCode: CategoryGroupCode? = null,
    val x: String? = null,
    val y: String? = null,
    val radius: Int? = null,
    val rect: String? = null,
    val page: Int = 1,
    val size: Int = 15,
    val sort: String = "accuracy",
)
