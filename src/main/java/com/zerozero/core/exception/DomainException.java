package com.zerozero.core.exception;

import com.zerozero.core.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ServiceException extends RunTimeCode {
  private final ErrorCode errorCode;
}