package com.zerozero.auth.exception;

import com.zerozero.core.support.error.CoreException;
import com.zerozero.core.support.error.ErrorType;

public class AuthException extends CoreException {

    public AuthException(ErrorType errorType) {
        super(errorType);
    }

    public AuthException(ErrorType errorType, Object data) {
        super(errorType, data);
    }
}
