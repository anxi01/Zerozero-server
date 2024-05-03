package com.zerozero.auth;

import com.zerozero.core.exception.ServiceException;
import com.zerozero.core.exception.error.ErrorCode;

public class DuplicateNicknameException extends ServiceException {

  public DuplicateNicknameException() {
    super(ErrorCode.DUPLICATE_NICKNAME_EXCEPTION);
  }
}