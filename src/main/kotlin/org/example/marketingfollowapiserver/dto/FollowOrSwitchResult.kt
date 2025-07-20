package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.FollowStatus

data class FollowOrSwitchResult(
    val followAdvertiserMetadata: FollowAdvertiserMetadata,
    val wasExisting: Boolean,
    val previousStatus: FollowStatus?
) {
    companion object {
        fun of(
            followAdvertiserMetadata: FollowAdvertiserMetadata,
            wasExisting: Boolean,
            previousStatus: FollowStatus?
        ): FollowOrSwitchResult {
            return FollowOrSwitchResult(
                followAdvertiserMetadata = followAdvertiserMetadata,
                wasExisting = wasExisting,
                previousStatus = previousStatus
            )
        }
    }
}
