package com.zerozero.review;

import com.zerozero.core.exception.error.ErrorCode;
import com.zerozero.core.exception.ServiceException;

public class ReviewNotFoundException extends ServiceException {

  public ReviewNotFoundException() {
    super(ErrorCode.REVIEW_NOT_FOUND_EXCEPTION);
  }
}
