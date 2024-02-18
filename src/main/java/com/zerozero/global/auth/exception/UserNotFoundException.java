package com.zerozero.global.auth.exception;

import com.zerozero.global.error.ErrorCode;
import com.zerozero.global.error.exception.ServiceException;

public class UserNotFoundException extends ServiceException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND_EXCEPTION);
  }
}
