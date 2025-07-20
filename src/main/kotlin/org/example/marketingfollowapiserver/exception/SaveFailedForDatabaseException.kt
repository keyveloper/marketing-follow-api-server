package org.example.marketingfollowapiserver.exception

import org.example.marketingfollowapiserver.enums.MSAServiceErrorCode
import org.springframework.http.HttpStatus

class SaveFailedForDatabaseException(
    override val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    override val msaServiceErrorCode: MSAServiceErrorCode = MSAServiceErrorCode.SAVE_FAILED_FOR_DATABASE,
    override val logics: String,
    override val message: String = "Failed to save data to database"
): MSAServerException(httpStatus, msaServiceErrorCode, logics, message)
