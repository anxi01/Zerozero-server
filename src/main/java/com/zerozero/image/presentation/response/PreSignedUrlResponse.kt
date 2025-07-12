package com.zerozero.image.presentation.response

import io.swagger.v3.oas.annotations.media.Schema

data class PreSignedUrlResponse(
    @Schema(
        description = "이미지 업로드 PreSigned URL",
        example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/0cbf3b99-b0ba-4148-a891-d04cb71ae236-test.png"
    )
    val preSignedUrl: String,

    @Schema(
        description = "이미지 객체 URL",
        example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/0cbf3b99-b0ba-4148-a891-d04cb71ae236-test.png"
    )
    val objectUrl: String
) {
    companion object {
        fun of(preSignedUrl: String, objectUrl: String) = PreSignedUrlResponse(preSignedUrl, objectUrl)
    }
}
