package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.FollowStatus
import java.util.UUID

data class FollowAdvertiserMetadata(
    val id: Long,
    val advertiserId: UUID,
    val influencerId: UUID,
    val followStatus: FollowStatus,
    val createdAt: Long,
    val lastModifiedAt: Long
) {
    companion object {
        fun of(
            id: Long,
            advertiserId: UUID,
            influencerId: UUID,
            followStatus: FollowStatus,
            createdAt: Long,
            lastModifiedAt: Long
        ): FollowAdvertiserMetadata {
            return FollowAdvertiserMetadata(
                id = id,
                advertiserId = advertiserId,
                influencerId = influencerId,
                followStatus = followStatus,
                createdAt = createdAt,
                lastModifiedAt = lastModifiedAt
            )
        }

        fun fromEntity(entity: FollowAdvertiserEntity): FollowAdvertiserMetadata {
            return FollowAdvertiserMetadata(
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
