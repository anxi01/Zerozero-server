package com.zerozero.auth;

import com.zerozero.core.exception.ServiceException;
import com.zerozero.core.exception.error.ErrorCode;

public class UserNotFoundException extends ServiceException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND_EXCEPTION);
  }
}
