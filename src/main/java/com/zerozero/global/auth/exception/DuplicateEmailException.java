package com.zerozero.global.auth.exception;

import com.zerozero.global.error.ErrorCode;
import com.zerozero.global.error.exception.ServiceException;

public class DuplicateEmailException extends ServiceException {

  public DuplicateEmailException() {
    super(ErrorCode.DUPLICATE_EMAIL_EXCEPTION);
  }
}