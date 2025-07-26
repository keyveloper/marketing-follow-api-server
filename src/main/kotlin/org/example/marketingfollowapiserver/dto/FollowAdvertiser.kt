package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.FollowStatus
import org.example.marketingfollowapiserver.table.FollowAdvertisersTable
import org.jetbrains.exposed.sql.ResultRow
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

        fun fromResultRow(row: ResultRow): FollowAdvertiser {
            return FollowAdvertiser(
                id = row[FollowAdvertisersTable.id].value,
                advertiserId = row[FollowAdvertisersTable.advertiserId],
                influencerId = row[FollowAdvertisersTable.influencerId],
                followStatus = row[FollowAdvertisersTable.followStatus],
                createdAt = row[FollowAdvertisersTable.createdAt],
                lastModifiedAt = row[FollowAdvertisersTable.lastModifiedAt]
            )
        }
    }
}
