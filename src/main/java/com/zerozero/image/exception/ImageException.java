package com.zerozero.image.exception;

import com.zerozero.core.support.error.CoreException;
import com.zerozero.core.support.error.ErrorType;

public class ImageException extends CoreException {

    public ImageException(ErrorType errorType) {
        super(errorType);
    }

    public ImageException(ErrorType errorType, Object data) {
        super(errorType, data);
    }
}
