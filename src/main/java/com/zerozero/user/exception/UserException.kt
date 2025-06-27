package com.zerozero.user.exception

import com.zerozero.core.support.error.CoreException
import com.zerozero.core.support.error.ErrorType

class UserException(errorType: ErrorType) : CoreException(errorType)
