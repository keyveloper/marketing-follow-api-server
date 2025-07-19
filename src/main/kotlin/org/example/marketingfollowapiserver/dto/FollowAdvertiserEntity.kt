package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.FollowStatus
import org.example.marketingfollowapiserver.table.FollowAdvertisersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class FollowAdvertiserEntity(id: EntityID<Long>): BaseDateEntity(id, FollowAdvertisersTable) {
    companion object: BaseDateEntityAutoUpdate<FollowAdvertiserEntity>(FollowAdvertisersTable)

    var advertiserId: UUID by FollowAdvertisersTable.advertiserId
    var influencerId: UUID by FollowAdvertisersTable.influencerId
    var followStatus: FollowStatus by FollowAdvertisersTable.followStatus
}
