package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.MSAServiceErrorCode
import org.springframework.http.HttpStatus

data class UnFollowResponseFromServer(
    val result: UnFollowResult,
    override val httpStatus: HttpStatus,
    override val msaServiceErrorCode: MSAServiceErrorCode,
    override val errorMessage: String?,
    override val logics: String?
): MSABusinessErrorResponse(httpStatus, msaServiceErrorCode, errorMessage, logics) {
    companion object {
        fun of(
            result: UnFollowResult,
            httpStatus: HttpStatus,
            msaServiceErrorCode: MSAServiceErrorCode,
            errorMessage: String?,
            logics: String?
        ): UnFollowResponseFromServer {
            return UnFollowResponseFromServer(
                result = result,
                httpStatus = httpStatus,
                msaServiceErrorCode = msaServiceErrorCode,
                errorMessage = errorMessage,
                logics = logics
            )
        }
    }
}
