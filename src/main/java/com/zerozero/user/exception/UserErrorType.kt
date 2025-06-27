package com.zerozero.user.exception

import com.zerozero.core.support.error.ErrorType
import org.springframework.http.HttpStatus

enum class UserErrorType(
    override val status: HttpStatus,
    override val message: String
) : ErrorType {
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    NOT_COMPLETED_MEMBER(HttpStatus.FORBIDDEN, "회원가입이 완료되지 않은 사용자입니다."),
    ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "이미 회원가입이 완료된 사용자입니다."),
    ;
}
