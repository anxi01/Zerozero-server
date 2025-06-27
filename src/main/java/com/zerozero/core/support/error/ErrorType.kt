package com.zerozero.core.support.error

import org.springframework.http.HttpStatus

interface ErrorType {
    val status: HttpStatus
    val message: String

    fun getCode(): String = (this as Enum<*>).name
}
