package org.example.marketingfollowapiserver.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.example.marketingfollowapiserver.dto.*
import org.example.marketingfollowapiserver.enums.FollowStatus
import org.example.marketingfollowapiserver.repository.FollowAdvertiserRepository
import org.springframework.stereotype.Service

@Service
class FollowService(
    private val followAdvertiserRepository: FollowAdvertiserRepository
) {
    private val logger = KotlinLogging.logger {}

    fun followOrSwitch(advertiserId: String, influencerId: String): FollowOrSwitchResult {
        logger.info { "followOrSwitch called: advertiserId=$advertiserId, influencerId=$influencerId" }

        val existingEntity = followAdvertiserRepository.findByAdvertiserIdAndInfluencerId(
            advertiserId = advertiserId,
            influencerId = influencerId
        )

        return if (existingEntity != null) {
            val previousStatus = existingEntity.followStatus
            logger.info { "Existing relationship found, switching status from $previousStatus" }

            val updatedMetadata = followAdvertiserRepository.switchFollowStatus(existingEntity)

            FollowOrSwitchResult.of(
                followAdvertiserMetadata = updatedMetadata,
                wasExisting = true,
                previousStatus = previousStatus
            )
        } else {
            logger.info { "No existing relationship found, creating new FOLLOW relationship" }

            val savedMetadata = followAdvertiserRepository.save(
                SaveFollowAdvertiser.of(
                    advertiserId = advertiserId,
                    influencerId = influencerId,
                    followStatus = FollowStatus.FOLLOW
                )
            )

            FollowOrSwitchResult.of(
                followAdvertiserMetadata = savedMetadata,
                wasExisting = false,
                previousStatus = null
            )
        }
    }

    fun getFollowersByAdvertiserId(advertiserId: String): GetFollowersResult {
        logger.info { "getFollowersByAdvertiserId called: advertiserId=$advertiserId" }

        val followers = followAdvertiserRepository.findFollowersByAdvertiserId(advertiserId)

        logger.info { "Found ${followers.size} followers for advertiserId=$advertiserId" }

        return GetFollowersResult.of(followers = followers)
    }

    fun getFollowingByInfluencerId(influencerId: String): GetFollowingResult {
        logger.info { "getFollowingByInfluencerId called: influencerId=$influencerId" }

        val following = followAdvertiserRepository.findFollowingByInfluencerId(influencerId)

        logger.info { "Found ${following.size} following for influencerId=$influencerId" }

        return GetFollowingResult.of(following = following)
    }
}
