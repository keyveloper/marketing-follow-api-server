package org.example.marketingfollowapiserver.dto

data class GetFollowersResultFromServer(
    val followers: List<FollowAdvertiser>
) {
    companion object {
        fun of(followers: List<FollowAdvertiser>): GetFollowersResultFromServer {
            return GetFollowersResultFromServer(followers = followers)
        }
    }
}
