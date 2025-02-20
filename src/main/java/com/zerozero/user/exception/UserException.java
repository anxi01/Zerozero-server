package com.zerozero.user.exception;

import com.zerozero.core.support.error.CoreException;
import com.zerozero.core.support.error.ErrorType;

public class UserException extends CoreException {

    public UserException(ErrorType errorType) {
        super(errorType);
    }

    public UserException(ErrorType errorType, Object data) {
        super(errorType, data);
    }
}
