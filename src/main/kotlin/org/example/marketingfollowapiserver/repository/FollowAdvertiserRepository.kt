package org.example.marketingfollowapiserver.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.example.marketingfollowapiserver.dto.FollowAdvertiser
import org.example.marketingfollowapiserver.dto.FollowAdvertiserEntity
import org.example.marketingfollowapiserver.enums.FollowStatus
import org.example.marketingfollowapiserver.exception.FailedFollowException
import org.example.marketingfollowapiserver.table.FollowAdvertisersTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upsert
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FollowAdvertiserRepository {
    private val logger = KotlinLogging.logger {}
    fun upsertFollow(
        advertiserId: UUID, influencerId: UUID, status: FollowStatus
    ): FollowAdvertiser {
        val now = System.currentTimeMillis()

        // Execute Exposed's type-safe upsert and get ResultRow
        val upsertStatement = FollowAdvertisersTable.upsert {
            it[FollowAdvertisersTable.advertiserId] = advertiserId
            it[FollowAdvertisersTable.influencerId] = influencerId
            it[followStatus] = status
            it[createdAt] = now
            it[lastModifiedAt] = now
        }

        val resultRow = upsertStatement.resultedValues?.get(0) ?: throw FailedFollowException(
            logics = "followAdRepo-upsert", message = "Failed Upsert"
        )

        // Convert ResultRow to FollowAdvertiser (no additional SELECT needed!)
        return FollowAdvertiser.fromResultRow(resultRow)
    }

    /**
     * Switch follow status (FOLLOW <-> UNFOLLOW) using DSL update
     * ⚠️ Must be called within a transaction
     *
     * @return Number of rows updated (0 if not found, 1 if updated)
     */
    fun unFollow(
        influencerId: UUID, advertiserId: UUID
    ): Int {
        return FollowAdvertisersTable.update(
            where = {
                (FollowAdvertisersTable.influencerId eq influencerId) and
                        (FollowAdvertisersTable.advertiserId eq advertiserId) and
                        (FollowAdvertisersTable.followStatus eq FollowStatus.FOLLOW)
            }
        ) { row ->
            row[FollowAdvertisersTable.followStatus] = FollowStatus.UNFOLLOW
            row[FollowAdvertisersTable.lastModifiedAt] = System.currentTimeMillis()
        }
    }

    /**
     * Find follow relationship by advertiser and influencer IDs
     * ⚠️ Must be called within a transaction
     */
    fun findByAdvertiserIdAndInfluencerId(advertiserId: UUID, influencerId: UUID): FollowAdvertiserEntity? {
        return FollowAdvertiserEntity.find {
            (FollowAdvertisersTable.advertiserId eq advertiserId) and (FollowAdvertisersTable.influencerId eq influencerId)
        }.firstOrNull()
    }

    /**
     * Find all followers (influencers following this advertiser)
     * ⚠️ Must be called within a transaction
     */
    fun findFollowersByAdvertiserId(advertiserId: UUID): List<FollowAdvertiser> {
        return FollowAdvertiserEntity.find {
            (FollowAdvertisersTable.advertiserId eq advertiserId) and (FollowAdvertisersTable.followStatus eq FollowStatus.FOLLOW)
        }.map { FollowAdvertiser.fromEntity(it) }
    }

    /**
     * Find all following (advertisers that this influencer is following)
     * ⚠️ Must be called within a transaction
     */
    fun findFollowingByInfluencerId(influencerId: UUID): List<FollowAdvertiser> {
        return FollowAdvertiserEntity.find {
            (FollowAdvertisersTable.influencerId eq influencerId) and (FollowAdvertisersTable.followStatus eq FollowStatus.FOLLOW)
        }.map { FollowAdvertiser.fromEntity(it) }
    }

    /**
     * Upsert follow relationship using Exposed's built-in upsert function
     * ⚠️ Must be called within a transaction
     *
     * ⚠️ Note: Exposed's upsert uses DSL, but still doesn't trigger EntityHook for updates.
     * lastModifiedAt is explicitly set in the upsert statement.
     *
     * @param advertiserId The advertiser UUID
     * @param influencerId The influencer UUID
     * @param status The follow status to set
     * @return The upserted FollowAdvertiser from ResultRow
     *
     * Behavior:
     * - Uses UNIQUE index on (advertiser_id, influencer_id) to detect conflicts
     * - If record doesn't exist: INSERT new record
     * - If record exists (conflict): UPDATE follow_status and last_modified_at
     * - createdAt is preserved on UPDATE (only set on INSERT)
     * - lastModifiedAt is always updated to current timestamp
     * - Returns ResultRow directly from upsert (no additional SELECT needed)
     */

}
