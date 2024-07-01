package com.zerozero.review;

import com.zerozero.core.exception.error.ErrorCode;
import com.zerozero.core.exception.ServiceException;

public class AlreadyReviewedException extends ServiceException {

  public AlreadyReviewedException() {
    super(ErrorCode.ALREADY_REVIEWED_EXCEPTION);
  }
}
