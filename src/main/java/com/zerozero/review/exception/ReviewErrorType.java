package com.zerozero.review.exception;

import com.zerozero.core.support.error.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorType implements ErrorType {
    NOT_EXIST_DELETABLE_REVIEW(HttpStatus.BAD_REQUEST, "삭제 가능한 리뷰가 존재하지 않습니다."),
    USER_VALIDATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "리뷰를 작성한 사용자가 아닙니다."),
    ALREADY_USER_REVIEWED(HttpStatus.INTERNAL_SERVER_ERROR, "이미 리뷰를 작성한 사용자입니다."),
    NOT_EXIST_REVIEW(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다."),
    ;

    private final HttpStatus status;

    private final String message;
}
