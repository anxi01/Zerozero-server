package com.zerozero.global.auth.exception;

import com.zerozero.global.error.ErrorCode;
import com.zerozero.global.error.exception.ServiceException;

public class DuplicateNicknameException extends ServiceException {

  public DuplicateNicknameException() {
    super(ErrorCode.DUPLICATE_NICKNAME_EXCEPTION);
  }
}