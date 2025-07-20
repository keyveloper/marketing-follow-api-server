package org.example.marketingfollowapiserver.exception

import org.example.marketingfollowapiserver.enums.MSAServiceErrorCode
import org.springframework.http.HttpStatus

class NotFoundFollowAdvertiserException(
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
    override val msaServiceErrorCode: MSAServiceErrorCode = MSAServiceErrorCode.NOT_FOUND_FOLLOW_ADVERTISER,
    override val logics: String,
    override val message: String = "Follow relationship not found"
): MSAServerException(httpStatus, msaServiceErrorCode, logics, message)
