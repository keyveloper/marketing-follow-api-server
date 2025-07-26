package org.example.marketingfollowapiserver.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.example.marketingfollowapiserver.dto.*
import org.example.marketingfollowapiserver.enums.MSAServiceErrorCode
import org.example.marketingfollowapiserver.service.FollowService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/follow")
class FollowController(
    private val followService: FollowService
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping("")
    fun follow(
        @RequestBody request: FollowApiRequest
    ): ResponseEntity<FollowResponseFromServer> {
        logger.info { "POST /api/follow/or-switch called with request: $request" }

        val result = followService.followUpsert(
            advertiserId = request.advertiserId,
            influencerId = request.influencerId
        )

        return ResponseEntity.ok(
            FollowResponseFromServer.of(
                httpStatus = HttpStatus.OK,
                msaServiceErrorCode = MSAServiceErrorCode.OK,
                errorMessage = null,
                logics = "FollowController.follow",
                result = result
            )
        )
    }

    @PostMapping("/unfollow")
    fun unfollow(
        @RequestBody request: UnFollowApiRequest
    ): ResponseEntity<UnFollowResponseFromServer> {
        val result = followService.unFollow(
            advertiserId = request.advertiserId,
            influencerId = request.influencerId
        )

        return ResponseEntity.ok(
            UnFollowResponseFromServer.of(
                result = result,
                httpStatus = HttpStatus.OK,
                msaServiceErrorCode = MSAServiceErrorCode.OK,
                errorMessage = null,
                logics = "followController.unFollow",
            )
        )
    }

    @GetMapping("/followers")
    fun getFollowersByAdvertiserId(
        @RequestParam advertiserId: UUID
    ): ResponseEntity<GetFollowersResponseFromServer> {
        logger.info { "GET /api/follow/followers called with advertiserId: $advertiserId" }

        val result = followService.getFollowersByAdvertiserId(advertiserId)

        return ResponseEntity.ok(
            GetFollowersResponseFromServer.of(
                httpStatus = HttpStatus.OK,
                msaServiceErrorCode = MSAServiceErrorCode.OK,
                errorMessage = null,
                logics = "FollowController.getFollowers",
                result = result
            )
        )
    }

    @GetMapping("/following")
    fun getFollowingByInfluencerId(
        @RequestParam influencerId: UUID
    ): ResponseEntity<GetFollowingResponseFromServer> {
        logger.info { "GET /api/follow/following called with influencerId: $influencerId" }

        val result = followService.getFollowingByInfluencerId(influencerId)

        return ResponseEntity.ok(
            GetFollowingResponseFromServer.of(
                httpStatus = HttpStatus.OK,
                msaServiceErrorCode = MSAServiceErrorCode.OK,
                errorMessage = null,
                logics = null,
                result = result
            )
        )
    }
}
