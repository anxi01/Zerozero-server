package com.zerozero.image.presentation.request

import io.swagger.v3.oas.annotations.media.Schema

@JvmRecord
data class PreSignedUrlRequest(

    @Schema(description = "이미지 접두사 ['store', 'user']", example = "store")
    val prefix: String,

    @Schema(description = "업로드된 이미지 파일명", example = "profile-image.png")
    val fileName: String
)
