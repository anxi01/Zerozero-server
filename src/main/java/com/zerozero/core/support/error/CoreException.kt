package com.zerozero.core.support.error

open class CoreException(
    val errorType: ErrorType
) : RuntimeException(errorType.message)
