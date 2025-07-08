package com.zerozero.image.presentation

import com.zerozero.configuration.interceptor.Authorization
import com.zerozero.configuration.swagger.ApiErrorCode
import com.zerozero.core.support.error.GlobalErrorType
import com.zerozero.core.support.response.ApiResponse
import com.zerozero.image.application.ReadPreSignedUrlUseCase
import com.zerozero.image.exception.ImageErrorType
import com.zerozero.image.presentation.request.PreSignedUrlRequest
import com.zerozero.image.presentation.response.PreSignedUrlResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Image", description = "이미지")
@RestController
class ReadPreSignedUrlController(
    private val readPreSignedUrlUseCase: ReadPreSignedUrlUseCase
) {

    @Operation(
        summary = "이미지 업로드 PreSigned URL 조회 API",
        description = "클라이언트에서 AWS S3 버킷에 이미지를 저장하기 위한 PreSigned URL을 조회합니다.",
        operationId = "/image/presigned-url"
    )
    @ApiErrorCode(
        GlobalErrorType::class, ImageErrorType::class
    )
    @Authorization
    @GetMapping("/image/presigned-url")
    fun readPreSignedUrl(
        @ParameterObject preSignedUrlRequest: PreSignedUrlRequest
    ): ApiResponse<PreSignedUrlResponse> {
        val preSignedUrlResponse = readPreSignedUrlUseCase.execute(preSignedUrlRequest)
        return ApiResponse.success(preSignedUrlResponse)
    }
}
