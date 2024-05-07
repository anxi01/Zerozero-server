package com.zerozero.global.error.exception;

import com.zerozero.global.common.dto.response.ApiResponse;
import com.zerozero.global.error.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

  @ExceptionHandler(ServiceException.class)
  public ApiResponse<?> handleException(ServiceException e) {
    ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());
    return ApiResponse.fail(errorResponse, errorResponse.getCode());
  }
}