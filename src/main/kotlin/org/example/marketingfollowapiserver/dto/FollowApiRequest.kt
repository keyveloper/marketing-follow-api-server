package org.example.marketingfollowapiserver.dto

import java.util.UUID

data class FollowApiRequest(
    val advertiserId: UUID,
    val influencerId: UUID
)