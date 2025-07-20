package org.example.marketingfollowapiserver.dto

data class GetFollowingResult(
    val following: List<FollowAdvertiserMetadata>
) {
    companion object {
        fun of(following: List<FollowAdvertiserMetadata>): GetFollowingResult {
            return GetFollowingResult(following = following)
        }
    }
}
