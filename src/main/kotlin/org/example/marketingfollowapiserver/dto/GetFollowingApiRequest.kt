package org.example.marketingfollowapiserver.dto

import java.util.UUID

data class GetFollowingApiRequest(
    val influencerId: UUID
) {
    companion object {
        fun of(influencerId: UUID): GetFollowingApiRequest {
            return GetFollowingApiRequest(influencerId = influencerId)
        }
    }
}
