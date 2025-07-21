package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.FollowStatus
import java.util.UUID

data class SaveFollow(
    val advertiserId: UUID,
    val influencerId: UUID,
    val followStatus: FollowStatus
) {
    companion object {
        fun of(
            advertiserId: UUID,
            influencerId: UUID,
            followStatus: FollowStatus
        ): SaveFollow {
            return SaveFollow(
                advertiserId = advertiserId,
                influencerId = influencerId,
                followStatus = followStatus
            )
        }
    }
}
