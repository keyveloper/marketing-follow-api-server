package org.example.marketingfollowapiserver.table

import org.example.marketingfollowapiserver.enums.FollowStatus
import org.jetbrains.exposed.sql.Column

object FollowAdvertisersTable : BaseDateLongIdTable("follow_advertisers") {
    val advertiserId: Column<String> = varchar("advertiser_id", 36)
    val influencerId: Column<String> = varchar("influencer_id", 36)
    val followStatus: Column<FollowStatus> = enumerationByName("follow_status", 20, FollowStatus::class)

    init {
        uniqueIndex(advertiserId, influencerId)
    }
}
