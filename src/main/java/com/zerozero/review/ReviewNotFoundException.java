package com.zerozero.review;

import com.zerozero.core.exception.ServiceException;
import com.zerozero.core.exception.error.ErrorCode;

public class ReviewNotFoundException extends ServiceException {

  public ReviewNotFoundException() {
    super(ErrorCode.REVIEW_NOT_FOUND_EXCEPTION);
  }
}
