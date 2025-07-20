package org.example.marketingfollowapiserver.exception

import org.example.marketingfollowapiserver.enums.MSAServiceErrorCode
import org.springframework.http.HttpStatus

open class MSAServerException(
    open val httpStatus: HttpStatus,
    open val msaServiceErrorCode: MSAServiceErrorCode,
    open val logics: String,
    override val message: String = "Undefined Business Exception. Please resign this Exception.",
): RuntimeException(message)
