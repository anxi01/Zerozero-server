package com.zerozero.store.exception;

import com.zerozero.core.support.error.CoreException;
import com.zerozero.core.support.error.ErrorType;

public class StoreException extends CoreException {

    public StoreException(ErrorType errorType) {
        super(errorType);
    }

    public StoreException(ErrorType errorType, Object data) {
        super(errorType, data);
    }
}
