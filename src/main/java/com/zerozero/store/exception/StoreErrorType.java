package com.zerozero.store.exception;

import com.zerozero.core.support.error.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StoreErrorType implements ErrorType {
    NOT_EXIST_STORE(HttpStatus.BAD_REQUEST, "판매점이 존재하지 않습니다."),
    NOT_EXIST_SEARCH_RESPONSE(HttpStatus.BAD_REQUEST, "검색 응답이 존재하지 않습니다."),
    ;

    private final HttpStatus status;

    private final String message;
}
