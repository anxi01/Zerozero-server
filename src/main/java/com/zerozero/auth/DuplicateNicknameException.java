package com.zerozero.auth;

import com.zerozero.core.exception.error.ErrorCode;
import com.zerozero.core.exception.ServiceException;

public class DuplicateNicknameException extends ServiceException {

  public DuplicateNicknameException() {
    super(ErrorCode.DUPLICATE_NICKNAME_EXCEPTION);
  }
}