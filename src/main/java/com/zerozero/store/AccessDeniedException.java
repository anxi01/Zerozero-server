package com.zerozero.store;

import com.zerozero.core.exception.ServiceException;
import com.zerozero.core.exception.error.ErrorCode;

public class AccessDeniedException extends ServiceException {

  public AccessDeniedException() {
    super(ErrorCode.ACCESS_DENIED_EXCEPTION);
  }
}
