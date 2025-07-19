package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.FollowStatus

data class FollowOrSwitchResult(
    val followAdvertiserMetadata: FollowAdvertiserMetadata,
    val wasExisting: Boolean,
    val followStatus: FollowStatus?
) {
    companion object {
        fun of(
            followAdvertiserMetadata: FollowAdvertiserMetadata,
            wasExisting: Boolean,
            followStatus: FollowStatus?
        ): FollowOrSwitchResult {
            return FollowOrSwitchResult(
                followAdvertiserMetadata = followAdvertiserMetadata,
                wasExisting = wasExisting,
                followStatus = followStatus
            )
        }
    }
}
