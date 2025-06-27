package com.zerozero.core.presentation

import com.zerozero.core.support.error.CoreException
import com.zerozero.core.support.error.GlobalErrorType
import com.zerozero.core.support.response.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(GlobalErrorType.INTERNAL_ERROR), GlobalErrorType.INTERNAL_ERROR.status)
    }

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ResponseEntity<ApiResponse<*>> {
        log.error("CoreException : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(e.errorType), e.errorType.status)
    }
}
