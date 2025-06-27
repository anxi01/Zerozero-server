package com.zerozero.store.exception

import com.zerozero.core.support.error.ErrorType
import org.springframework.http.HttpStatus

enum class StoreErrorType(
    override val status: HttpStatus,
    override val message: String
) : ErrorType {
    NOT_EXIST_STORE(HttpStatus.BAD_REQUEST, "판매점이 존재하지 않습니다."),
    NOT_EXIST_SEARCH_RESPONSE(HttpStatus.BAD_REQUEST, "검색 응답이 존재하지 않습니다."),
    USER_RANK_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "해당 유저의 랭킹 정보를 확인할 수 없습니다."),
    ;
}
