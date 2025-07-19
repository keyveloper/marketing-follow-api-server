package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.FollowStatus
import java.util.UUID

data class SaveFollowAdvertiser(
    val advertiserId: UUID,
    val influencerId: UUID,
    val followStatus: FollowStatus
) {
    companion object {
        fun of(
            advertiserId: UUID,
            influencerId: UUID,
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
