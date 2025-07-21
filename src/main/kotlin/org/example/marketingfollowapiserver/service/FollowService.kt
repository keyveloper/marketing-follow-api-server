package org.example.marketingfollowapiserver.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.example.marketingfollowapiserver.dto.*
import org.example.marketingfollowapiserver.enums.FollowStatus
import org.example.marketingfollowapiserver.repository.FollowAdvertiserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FollowService(
    private val followAdvertiserRepository: FollowAdvertiserRepository
) {
    private val logger = KotlinLogging.logger {}

    fun followOrSwitch(advertiserId: UUID, influencerId: UUID): FollowOrSwitchResult {
        logger.info { "followOrSwitch called: advertiserId=$advertiserId, influencerId=$influencerId" }

        val existingEntity = followAdvertiserRepository.findByAdvertiserIdAndInfluencerId(
            advertiserId = advertiserId,
            influencerId = influencerId
        )

        return if (existingEntity != null) {
            val followStatus = existingEntity.followStatus
            logger.info { "Existing relationship found, switching status from $followStatus" }

            val updatedMetadata = followAdvertiserRepository.switchFollowStatus(existingEntity)

            FollowOrSwitchResult.of(
                followAdvertiser = updatedMetadata,
                wasExisting = true,
                followStatus = followStatus
            )
        } else {
            logger.info { "No existing relationship found, creating new FOLLOW relationship" }

            val savedMetadata = followAdvertiserRepository.save(
                SaveFollow.of(
                    advertiserId = advertiserId,
                    influencerId = influencerId,
                    followStatus = FollowStatus.FOLLOW
                )
            )

            FollowOrSwitchResult.of(
                followAdvertiser = savedMetadata,
                wasExisting = false,
                followStatus = null
            )
        }
    }

    fun getFollowersByAdvertiserId(advertiserId: UUID): GetFollowersResult {
        logger.info { "getFollowersByAdvertiserId called: advertiserId=$advertiserId" }

        val followers = followAdvertiserRepository.findFollowersByAdvertiserId(advertiserId)

        logger.info { "Found ${followers.size} followers for advertiserId=$advertiserId" }

        return GetFollowersResult.of(followers = followers)
    }

    fun getFollowingByInfluencerId(influencerId: UUID): GetFollowingResult {
        logger.info { "getFollowingByInfluencerId called: influencerId=$influencerId" }

        val following = followAdvertiserRepository.findFollowingByInfluencerId(influencerId)

        logger.info { "Found ${following.size} following for influencerId=$influencerId" }

        return GetFollowingResult.of(following = following)
    }
}
