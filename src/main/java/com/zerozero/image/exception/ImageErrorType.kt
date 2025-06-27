package com.zerozero.image.exception

import com.zerozero.core.support.error.ErrorType
import org.springframework.http.HttpStatus

enum class ImageErrorType(
    override val status: HttpStatus,
    override val message: String
) : ErrorType {
    INVALID_IMAGE_PREFIX(HttpStatus.BAD_REQUEST, "유효하지 않은 prefix 값입니다."),
    INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, "유효하지 않은 확장자입니다."),
    FAILED_TO_MAKE_URL(HttpStatus.INTERNAL_SERVER_ERROR, "PreSigned URL 생성에 실패했습니다."),
    ;
}
