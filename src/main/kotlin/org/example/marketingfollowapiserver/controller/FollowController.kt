package org.example.marketingfollowapiserver.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.example.marketingfollowapiserver.dto.*
import org.example.marketingfollowapiserver.enums.MSAServiceErrorCode
import org.example.marketingfollowapiserver.service.FollowService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/follow")
class FollowController(
    private val followService: FollowService
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping("/or-switch")
    fun followOrSwitch(
        @RequestBody request: FollowOrSwitchApiRequest
    ): FollowOrSwitchResponseFromServer {
        logger.info { "POST /api/follow/or-switch called with request: $request" }

        val result = followService.followOrSwitch(
            advertiserId = request.advertiserId,
            influencerId = request.influencerId
        )

        return FollowOrSwitchResponseFromServer.of(
            httpStatus = HttpStatus.OK,
            msaServiceErrorCode = MSAServiceErrorCode.OK,
            errorMessage = null,
            logics = "FollowController.followOrSwitch",
            result = result
        )
    }

    @GetMapping("/followers")
    fun getFollowers(
        @RequestParam advertiserId: UUID
    ): GetFollowersResponseFromServer {
        logger.info { "GET /api/follow/followers called with advertiserId: $advertiserId" }

        val result = followService.getFollowersByAdvertiserId(advertiserId)

        return GetFollowersResponseFromServer.of(
            httpStatus = HttpStatus.OK,
            msaServiceErrorCode = MSAServiceErrorCode.OK,
            errorMessage = null,
            logics = "FollowController.getFollowers",
            result = result
        )
    }

    @GetMapping("/following")
    fun getFollowing(
        @RequestParam influencerId: UUID
    ): GetFollowingResponseFromServer {
        logger.info { "GET /api/follow/following called with influencerId: $influencerId" }

        val result = followService.getFollowingByInfluencerId(influencerId)

        return GetFollowingResponseFromServer.of(
            httpStatus = HttpStatus.OK,
            msaServiceErrorCode = MSAServiceErrorCode.OK,
            errorMessage = null,
            logics = "FollowController.getFollowing",
            result = result
        )
    }
}
