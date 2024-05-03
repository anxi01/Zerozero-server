package com.zerozero.review;

import com.zerozero.core.exception.ServiceException;
import com.zerozero.core.exception.error.ErrorCode;

public class AlreadyReviewedException extends ServiceException {

  public AlreadyReviewedException() {
    super(ErrorCode.ALREADY_REVIEWED_EXCEPTION);
  }
}
