package com.zerozero.core.presentation;

import com.zerozero.core.exception.DomainException;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

  private int statusCode;

  private boolean success;

  private String message;

  private String code;

  @Builder(builderClassName = "CreateErrorCode", builderMethodName = "createErrorCode")
  public ErrorResponse(int statusCode, Exception exception) {
    this.statusCode = statusCode;
    this.success = false;
    this.message = exception.getMessage();
    this.code = exception.getClass().getSimpleName();
  }

  @Builder(builderClassName = "CreateDomainErrorCode", builderMethodName = "createDomainErrorCode")
  public ErrorResponse(int statusCode, DomainException exception) {
    this.statusCode = statusCode;
    this.success = false;
    this.message = exception.getMessage();
    this.code = exception.getCode();
  }
}
