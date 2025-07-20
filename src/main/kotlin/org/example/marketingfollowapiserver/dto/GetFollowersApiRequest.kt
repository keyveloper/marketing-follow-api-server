package org.example.marketingfollowapiserver.dto

data class GetFollowersApiRequest(
    val advertiserId: String
) {
    companion object {
        fun of(advertiserId: String): GetFollowersApiRequest {
            return GetFollowersApiRequest(advertiserId = advertiserId)
        }
    }
}
