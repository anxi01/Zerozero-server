package com.zerozero.auth;

import com.zerozero.core.exception.ServiceException;
import com.zerozero.core.exception.error.ErrorCode;

public class InvalidTokenException extends ServiceException {

  public InvalidTokenException() {
    super(ErrorCode.INVALID_TOKEN_EXCEPTION);
  }
}