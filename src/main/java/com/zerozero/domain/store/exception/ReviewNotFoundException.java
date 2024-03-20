package com.zerozero.domain.store.exception;

import com.zerozero.global.error.ErrorCode;
import com.zerozero.global.error.exception.ServiceException;

public class ReviewNotFoundException extends ServiceException {

  public ReviewNotFoundException() {
    super(ErrorCode.REVIEW_NOT_FOUND_EXCEPTION);
  }
}
