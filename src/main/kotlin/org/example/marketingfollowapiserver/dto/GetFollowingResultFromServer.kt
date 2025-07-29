package org.example.marketingfollowapiserver.dto

data class GetFollowingResultFromServer(
    val following: List<FollowAdvertiser>
) {
    companion object {
        fun of(following: List<FollowAdvertiser>): GetFollowingResultFromServer {
            return GetFollowingResultFromServer(following = following)
        }
    }
}
