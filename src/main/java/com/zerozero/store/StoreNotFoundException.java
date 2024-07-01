package com.zerozero.store;

import com.zerozero.core.exception.error.ErrorCode;
import com.zerozero.core.exception.ServiceException;

public class StoreNotFoundException extends ServiceException {

  public StoreNotFoundException() {
    super(ErrorCode.STORE_NOT_FOUND_EXCEPTION);
  }
}
