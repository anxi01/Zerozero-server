package com.zerozero.review.exception

import com.zerozero.core.support.error.ErrorType
import org.springframework.http.HttpStatus

enum class ReviewErrorType(
    override val status: HttpStatus,
    override val message: String
) : ErrorType {
    NOT_EXIST_DELETABLE_REVIEW(HttpStatus.BAD_REQUEST, "삭제 가능한 리뷰가 존재하지 않습니다."),
    USER_VALIDATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "리뷰를 작성한 사용자가 아닙니다."),
    ALREADY_USER_REVIEWED(HttpStatus.INTERNAL_SERVER_ERROR, "이미 리뷰를 작성한 사용자입니다."),
    NOT_EXIST_REVIEW(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다."),
    ;
}
