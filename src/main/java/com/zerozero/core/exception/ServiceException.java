package com.zerozero.core.exception;

import com.zerozero.core.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceException extends RuntimeException {
  private final ErrorCode errorCode;
}