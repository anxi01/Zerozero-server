package com.zerozero.store;

import com.zerozero.core.exception.error.ErrorCode;
import com.zerozero.core.exception.ServiceException;

public class AccessDeniedException extends ServiceException {

  public AccessDeniedException() {
    super(ErrorCode.ACCESS_DENIED_EXCEPTION);
  }
}
