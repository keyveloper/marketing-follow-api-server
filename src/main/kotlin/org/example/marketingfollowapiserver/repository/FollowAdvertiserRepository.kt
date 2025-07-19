package org.example.marketingfollowapiserver.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.example.marketingfollowapiserver.dto.FollowAdvertiserEntity
import org.example.marketingfollowapiserver.dto.FollowAdvertiserMetadata
import org.example.marketingfollowapiserver.dto.SaveFollowAdvertiser
import org.example.marketingfollowapiserver.enums.FollowStatus
import org.example.marketingfollowapiserver.exception.SaveFailedForDatabaseException
import org.example.marketingfollowapiserver.table.FollowAdvertisersTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class FollowAdvertiserRepository {
    private val logger = KotlinLogging.logger {}

    fun save(saveFollowAdvertiser: SaveFollowAdvertiser): FollowAdvertiserMetadata {
        return try {
            transaction {
                val entity = FollowAdvertiserEntity.new {
                    advertiserId = saveFollowAdvertiser.advertiserId
                    influencerId = saveFollowAdvertiser.influencerId
                    followStatus = saveFollowAdvertiser.followStatus
                }
                FollowAdvertiserMetadata.fromEntity(entity)
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to save FollowAdvertiser: $saveFollowAdvertiser" }
            throw SaveFailedForDatabaseException(
                logics = "FollowAdvertiserRepository.save",
                message = "Failed to save follow relationship: ${e.message}"
            )
        }
    }

    fun switchFollowStatus(followAdvertiserEntity: FollowAdvertiserEntity): FollowAdvertiserMetadata {
        return try {
            transaction {
                followAdvertiserEntity.followStatus = when (followAdvertiserEntity.followStatus) {
                    FollowStatus.FOLLOW -> FollowStatus.UNFOLLOW
                    FollowStatus.UNFOLLOW -> FollowStatus.FOLLOW
                }
                FollowAdvertiserMetadata.fromEntity(followAdvertiserEntity)
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to switch follow status for entity: ${followAdvertiserEntity.id}" }
            throw SaveFailedForDatabaseException(
                logics = "FollowAdvertiserRepository.switchFollowStatus",
                message = "Failed to switch follow status: ${e.message}"
            )
        }
    }

    fun findByAdvertiserIdAndInfluencerId(advertiserId: UUID, influencerId: UUID): FollowAdvertiserEntity? {
        return transaction {
            FollowAdvertiserEntity.find {
                (FollowAdvertisersTable.advertiserId eq advertiserId) and
                        (FollowAdvertisersTable.influencerId eq influencerId)
            }.firstOrNull()
        }
    }

    fun findFollowersByAdvertiserId(advertiserId: UUID): List<FollowAdvertiserMetadata> {
        return transaction {
            FollowAdvertiserEntity.find {
                (FollowAdvertisersTable.advertiserId eq advertiserId) and
                        (FollowAdvertisersTable.followStatus eq FollowStatus.FOLLOW)
            }.map { FollowAdvertiserMetadata.fromEntity(it) }
        }
    }

    fun findFollowingByInfluencerId(influencerId: UUID): List<FollowAdvertiserMetadata> {
        return transaction {
            FollowAdvertiserEntity.find {
                (FollowAdvertisersTable.influencerId eq influencerId) and
                        (FollowAdvertisersTable.followStatus eq FollowStatus.FOLLOW)
            }.map { FollowAdvertiserMetadata.fromEntity(it) }
        }
    }
}
