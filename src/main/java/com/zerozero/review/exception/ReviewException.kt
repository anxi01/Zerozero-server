package com.zerozero.review.exception

import com.zerozero.core.support.error.CoreException
import com.zerozero.core.support.error.ErrorType

class ReviewException(errorType: ErrorType) : CoreException(errorType)
