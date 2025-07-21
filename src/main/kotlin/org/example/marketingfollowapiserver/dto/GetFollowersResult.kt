package org.example.marketingfollowapiserver.dto

data class GetFollowersResult(
    val followers: List<FollowAdvertiser>
) {
    companion object {
        fun of(followers: List<FollowAdvertiser>): GetFollowersResult {
            return GetFollowersResult(followers = followers)
        }
    }
}
