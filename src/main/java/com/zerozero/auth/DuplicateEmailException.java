package com.zerozero.auth;

import com.zerozero.core.exception.error.ErrorCode;
import com.zerozero.core.exception.ServiceException;

public class DuplicateEmailException extends ServiceException {

  public DuplicateEmailException() {
    super(ErrorCode.DUPLICATE_EMAIL_EXCEPTION);
  }
}