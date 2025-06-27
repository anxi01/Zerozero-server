package com.zerozero.core.support.error

import org.springframework.http.HttpStatus

enum class GlobalErrorType(
    override val status: HttpStatus,
    override val message: String,
) : ErrorType {

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 내부 오류입니다."),
    KAKAO_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "카카오 서비스가 응답하지 않습니다."), ;
}
