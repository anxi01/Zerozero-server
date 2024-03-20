package com.zerozero.domain.store.exception;

import com.zerozero.global.error.ErrorCode;
import com.zerozero.global.error.exception.ServiceException;

public class AccessDeniedException extends ServiceException {

  public AccessDeniedException() {
    super(ErrorCode.ACCESS_DENIED_EXCEPTION);
  }
}
