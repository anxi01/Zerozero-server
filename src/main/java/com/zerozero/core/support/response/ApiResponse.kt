package com.zerozero.core.support.response

import com.zerozero.core.support.error.ErrorMessage
import com.zerozero.core.support.error.ErrorType

@JvmRecord
data class ApiResponse<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val error: ErrorMessage? = null
) {
    companion object {
        @JvmStatic
        fun success(): ApiResponse<Any> {
            return ApiResponse(ResultType.SUCCESS, null, null)
        }

        @JvmStatic
        fun <S> success(data: S): ApiResponse<S> {
            return ApiResponse(ResultType.SUCCESS, data, null)
        }

        @JvmStatic
        fun error(error: ErrorType): ApiResponse<Any> {
            return ApiResponse(ResultType.ERROR, null, ErrorMessage(error))
        }
    }
}
