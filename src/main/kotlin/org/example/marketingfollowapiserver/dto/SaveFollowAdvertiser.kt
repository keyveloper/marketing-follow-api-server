package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.FollowStatus

data class SaveFollowAdvertiser(
    val advertiserId: String,
    val influencerId: String,
    val followStatus: FollowStatus
) {
    companion object {
        fun of(
            advertiserId: String,
            influencerId: String,
            followStatus: FollowStatus
        ): SaveFollowAdvertiser {
            return SaveFollowAdvertiser(
                advertiserId = advertiserId,
                influencerId = influencerId,
                followStatus = followStatus
            )
        }
    }
}
