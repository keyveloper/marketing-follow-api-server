package org.example.marketingfollowapiserver.dto

import java.util.UUID

data class GetFollowersApiRequest(
    val advertiserId: UUID
) {
    companion object {
        fun of(advertiserId: UUID): GetFollowersApiRequest {
            return GetFollowersApiRequest(advertiserId = advertiserId)
        }
    }
}
