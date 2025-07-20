package org.example.marketingfollowapiserver.dto

data class FollowOrSwitchApiRequest(
    val advertiserId: String,
    val influencerId: String
) {
    companion object {
        fun of(
            advertiserId: String,
            influencerId: String
        ): FollowOrSwitchApiRequest {
            return FollowOrSwitchApiRequest(
                advertiserId = advertiserId,
                influencerId = influencerId
            )
        }
    }
}
