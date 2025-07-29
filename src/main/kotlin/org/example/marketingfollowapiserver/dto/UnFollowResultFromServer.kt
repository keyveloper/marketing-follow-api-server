package org.example.marketingfollowapiserver.dto

data class UnFollowResultFromServer(
    val effectedRow: Int
) {
    companion object {
        fun of(effectedRow: Int): UnFollowResultFromServer {
            return UnFollowResultFromServer(effectedRow)
        }
    }
}