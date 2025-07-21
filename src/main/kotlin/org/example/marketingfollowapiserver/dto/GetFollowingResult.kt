package org.example.marketingfollowapiserver.dto

data class GetFollowingResult(
    val following: List<FollowAdvertiser>
) {
    companion object {
        fun of(following: List<FollowAdvertiser>): GetFollowingResult {
            return GetFollowingResult(following = following)
        }
    }
}
