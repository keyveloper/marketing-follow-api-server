package org.example.marketingfollowapiserver.dto

import java.util.UUID

data class FollowOrSwitchApiRequest(
    val advertiserId: UUID,
    val influencerId: UUID
) {
    companion object {
        fun of(
            advertiserId: UUID,
            influencerId: UUID
        ): FollowOrSwitchApiRequest {
            return FollowOrSwitchApiRequest(
                advertiserId = advertiserId,
                influencerId = influencerId
            )
        }
    }
}
