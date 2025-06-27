package com.zerozero.store.exception

import com.zerozero.core.support.error.CoreException
import com.zerozero.core.support.error.ErrorType

class StoreException(errorType: ErrorType) : CoreException(errorType)
