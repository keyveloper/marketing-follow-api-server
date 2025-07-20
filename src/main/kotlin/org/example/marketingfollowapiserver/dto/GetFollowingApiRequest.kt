package org.example.marketingfollowapiserver.dto

data class GetFollowingApiRequest(
    val influencerId: String
) {
    companion object {
        fun of(influencerId: String): GetFollowingApiRequest {
            return GetFollowingApiRequest(influencerId = influencerId)
        }
    }
}
