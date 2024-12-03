package com.zerozero.core.exception;

import com.zerozero.core.presentation.ErrorResponse;
import java.util.Optional;

import io.sentry.Sentry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    ErrorResponse errorResponse = ErrorResponse.createErrorCode().statusCode(500).exception(e).build();
    Sentry.captureException(e);
    return ResponseEntity.internalServerError().body(errorResponse);
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorResponse> handleException(DomainException e) {
    HttpStatus httpStatus = Optional.ofNullable(e.getHttpStatus()).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    ErrorResponse errorResponse = ErrorResponse.createDomainErrorCode().statusCode(httpStatus.value()).exception(e).build();
    Sentry.captureException(e);
    return ResponseEntity.status(httpStatus).body(errorResponse);
  }
}
