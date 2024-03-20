package com.zerozero.domain.store.exception;

import com.zerozero.global.error.ErrorCode;
import com.zerozero.global.error.exception.ServiceException;

public class AlreadyReviewedException extends ServiceException {

  public AlreadyReviewedException() {
    super(ErrorCode.ALREADY_REVIEWED_EXCEPTION);
  }
}
