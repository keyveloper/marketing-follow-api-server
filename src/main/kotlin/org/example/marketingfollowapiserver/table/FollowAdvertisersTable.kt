package org.example.marketingfollowapiserver.table

import org.example.marketingfollowapiserver.enums.FollowStatus
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object FollowAdvertisersTable : BaseDateLongIdTable("follow_advertisers") {
    val advertiserId: Column<UUID> = uuid("advertiser_id")
    val influencerId: Column<UUID> = uuid("influencer_id")
    val followStatus: Column<FollowStatus> =
        enumerationByName("follow_status", 20, FollowStatus::class)

    init {
        uniqueIndex(advertiserId, influencerId)
    }
}
