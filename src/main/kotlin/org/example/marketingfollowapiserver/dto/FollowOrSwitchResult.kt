package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.FollowStatus

data class FollowOrSwitchResult(
    val followAdvertiser: FollowAdvertiser,
    val wasExisting: Boolean,
    val followStatus: FollowStatus?
) {
    companion object {
        fun of(
            followAdvertiser: FollowAdvertiser,
            wasExisting: Boolean,
            followStatus: FollowStatus?
        ): FollowOrSwitchResult {
            return FollowOrSwitchResult(
                followAdvertiser = followAdvertiser,
                wasExisting = wasExisting,
                followStatus = followStatus
            )
        }
    }
}
