package com.zerozero.core.exception;

import com.zerozero.core.presentation.ApiResponse;
import com.zerozero.core.presentation.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

  @ExceptionHandler(ServiceException.class)
  public ApiResponse<?> handleException(ServiceException e) {
    ErrorResponse errorResponse = ErrorResponse.from(e.getErrorCode());
    return ApiResponse.fail(errorResponse, errorResponse.getCode());
  }
}