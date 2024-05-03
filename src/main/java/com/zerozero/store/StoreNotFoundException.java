package com.zerozero.store;

import com.zerozero.core.exception.ServiceException;
import com.zerozero.core.exception.error.ErrorCode;

public class StoreNotFoundException extends ServiceException {

  public StoreNotFoundException() {
    super(ErrorCode.STORE_NOT_FOUND_EXCEPTION);
  }
}
