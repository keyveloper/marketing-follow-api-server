package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.MSAServiceErrorCode
import org.springframework.http.HttpStatus

data class GetFollowingResponseFromServer(
    override val httpStatus: HttpStatus,
    override val msaServiceErrorCode: MSAServiceErrorCode,
    override val errorMessage: String? = null,
    override val logics: String? = null,
    val result: GetFollowingResult?
) : MSABusinessErrorResponse(httpStatus, msaServiceErrorCode, errorMessage, logics) {
    companion object {
        fun of(
            httpStatus: HttpStatus,
            msaServiceErrorCode: MSAServiceErrorCode,
            errorMessage: String?,
            logics: String,
            result: GetFollowingResult?
        ): GetFollowingResponseFromServer {
            return GetFollowingResponseFromServer(
                httpStatus = httpStatus,
                msaServiceErrorCode = msaServiceErrorCode,
                errorMessage = errorMessage,
                logics = logics,
                result = result
            )
        }
    }
}
