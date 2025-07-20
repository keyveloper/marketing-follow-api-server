package org.example.marketingfollowapiserver.dto

data class GetFollowersResult(
    val followers: List<FollowAdvertiserMetadata>
) {
    companion object {
        fun of(followers: List<FollowAdvertiserMetadata>): GetFollowersResult {
            return GetFollowersResult(followers = followers)
        }
    }
}
