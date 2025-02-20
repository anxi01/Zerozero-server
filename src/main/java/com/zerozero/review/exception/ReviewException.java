package com.zerozero.review.exception;

import com.zerozero.core.support.error.CoreException;
import com.zerozero.core.support.error.ErrorType;

public class ReviewException extends CoreException {

    public ReviewException(ErrorType errorType) {
        super(errorType);
    }

    public ReviewException(ErrorType errorType, Object data) {
        super(errorType, data);
    }
}
