package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.FollowStatus
import java.util.UUID

data class FollowAdvertiser(
    val id: Long,
    val advertiserId: UUID,
    val influencerId: UUID,
    val followStatus: FollowStatus,
    val createdAt: Long,
    val lastModifiedAt: Long
) {
    companion object {
        fun fromEntity(entity: FollowAdvertiserEntity): FollowAdvertiser {
            return FollowAdvertiser(
                id = entity.id.value,
                advertiserId = entity.advertiserId,
                influencerId = entity.influencerId,
                followStatus = entity.followStatus,
                createdAt = entity.createdAt,
                lastModifiedAt = entity.lastModifiedAt
            )
        }
    }
}
