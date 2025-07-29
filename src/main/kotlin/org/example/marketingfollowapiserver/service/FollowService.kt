package org.example.marketingfollowapiserver.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.example.marketingfollowapiserver.dto.*
import org.example.marketingfollowapiserver.enums.FollowStatus
import org.example.marketingfollowapiserver.exception.FailedFollowException
import org.example.marketingfollowapiserver.repository.FollowAdvertiserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FollowService(
    private val followAdvertiserRepository: FollowAdvertiserRepository
) {
    private val logger = KotlinLogging.logger {}

    fun followUpsert(influencerId: UUID, advertiserId: UUID): FollowAdvertiser {
        return transaction {
            followAdvertiserRepository.upsertFollow(
                advertiserId,
                influencerId,
                FollowStatus.FOLLOW)
        }
    }

    fun unFollow(influencerId: UUID, advertiserId: UUID): UnFollowResultFromServer {
        return transaction {
            val effectedRow = followAdvertiserRepository.unFollowStatusByUserIds(
                influencerId, advertiserId
            )

            if (effectedRow == 0) throw FailedFollowException(
                logics = "FollowSvc-unFollow",
                message = "unfollow Failed maybe can't find follow entity influencerId" +
                        " = ${influencerId}, advertiserId = $advertiserId"
            )

            UnFollowResultFromServer.of(effectedRow)
        }
    }

    /**
     * Follow or switch follow status
     * Transaction managed at service layer
     */

    /**
     * Get all followers for an advertiser
     * Transaction managed at service layer
     */
    fun getFollowersByAdvertiserId(advertiserId: UUID): GetFollowersResultFromServer {
        logger.info { "getFollowersByAdvertiserId called: advertiserId=$advertiserId" }

        return transaction {
            val followers = followAdvertiserRepository.findFollowersByAdvertiserId(advertiserId)

            logger.info { "Found ${followers.size} followers for advertiserId=$advertiserId" }

            GetFollowersResultFromServer.of(followers = followers)
        }
    }

    /**
     * Get all following for an influencer
     * Transaction managed at service layer
     */
    fun getFollowingByInfluencerId(influencerId: UUID): GetFollowingResultFromServer {
        logger.info { "getFollowingByInfluencerId called: influencerId=$influencerId" }

        return transaction {
            val following = followAdvertiserRepository.findFollowingByInfluencerId(influencerId)

            logger.info { "Found ${following.size} following for influencerId=$influencerId" }

            GetFollowingResultFromServer.of(following = following)
        }
    }
}
