package com.zerozero.domain.store.exception;

import com.zerozero.global.error.ErrorCode;
import com.zerozero.global.error.exception.ServiceException;

public class StoreNotFoundException extends ServiceException {

  public StoreNotFoundException() {
    super(ErrorCode.STORE_NOT_FOUND_EXCEPTION);
  }
}
