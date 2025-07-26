package org.example.marketingfollowapiserver.dto

data class UnFollowResult(
    val effectedRow: Int
) {
    companion object {
        fun of(effectedRow: Int): UnFollowResult {
            return UnFollowResult(effectedRow)
        }
    }
}