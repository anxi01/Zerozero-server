package com.zerozero.core.support.error

data class ErrorMessage(
    val code: String,
    val message: String
) {
    constructor(errorType: ErrorType) : this(
        code = errorType.getCode(),
        message = errorType.message
    )
}
