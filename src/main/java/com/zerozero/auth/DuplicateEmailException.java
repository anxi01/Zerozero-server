package com.zerozero.auth;

import com.zerozero.core.exception.ServiceException;
import com.zerozero.core.exception.error.ErrorCode;

public class DuplicateEmailException extends ServiceException {

  public DuplicateEmailException() {
    super(ErrorCode.DUPLICATE_EMAIL_EXCEPTION);
  }
}