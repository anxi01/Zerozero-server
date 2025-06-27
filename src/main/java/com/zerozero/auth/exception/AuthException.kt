package com.zerozero.auth.exception

import com.zerozero.core.support.error.CoreException
import com.zerozero.core.support.error.ErrorType

class AuthException(errorType: ErrorType) : CoreException(errorType)
