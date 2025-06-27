package com.zerozero.auth.exception

import com.zerozero.core.support.error.ErrorType
import org.springframework.http.HttpStatus

enum class AuthErrorType(
    override val status: HttpStatus,
    override val message: String
) : ErrorType {
    NOT_EXIST_HEADER(HttpStatus.UNAUTHORIZED, "Authorization Header가 존재하지 않습니다."),
    NOT_EXIST_TOKEN(HttpStatus.UNAUTHORIZED, "Authorization Header에 Token이 존재하지 않습니다."),
    NOT_DEFINE_TOKEN(HttpStatus.UNAUTHORIZED, "정의되지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_EXIST_PROVIDER(HttpStatus.BAD_REQUEST, "OAuth 써드파티 제공자가 존재하지 않습니다."),
    INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "유효한 OAuth 써드파티 제공자가 아닙니다."),
    NOT_EXIST_AUTH_CODE(HttpStatus.BAD_GATEWAY, "OAuth 써드파티 제공자에서 제공받은 인증 코드가 존재하지 않습니다."),
    ACCESS_TOKEN_NOT_ISSUED(HttpStatus.BAD_GATEWAY, "OAuth 써드파티 제공자에서 액세스 토큰이 발급되지 않았습니다."),
    NOT_EXIST_RESOURCE_RESPONSE(HttpStatus.BAD_GATEWAY, "OAuth 써드파티 리소스 서버에서 자원이 존재하지 않습니다."),
    RESOURCE_SERVER_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "OAuth Resource Server에 접근할 수 없습니다."),
    NOT_EXIST_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰이 존재하지 않습니다."),
    TOKEN_REFRESH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 재발급에 실패하였습니다."),
    ;
}
