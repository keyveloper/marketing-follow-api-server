package org.example.marketingfollowapiserver.dto

import java.util.UUID

data class UnFollowApiRequest(
    val advertiserId: UUID,
    val influencerId: UUID
)