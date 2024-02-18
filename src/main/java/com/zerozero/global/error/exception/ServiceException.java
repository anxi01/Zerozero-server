package com.zerozero.global.error.exception;

import com.zerozero.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceException extends RuntimeException {
  private final ErrorCode errorCode;
}