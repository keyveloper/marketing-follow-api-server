package org.example.marketingfollowapiserver.dto

import org.example.marketingfollowapiserver.enums.FollowStatus
import org.example.marketingfollowapiserver.table.FollowAdvertisersTable
import org.jetbrains.exposed.dao.id.EntityID

class FollowAdvertiserEntity(id: EntityID<Long>): BaseDateEntity(id, FollowAdvertisersTable) {
    companion object: BaseDateEntityAutoUpdate<FollowAdvertiserEntity>(FollowAdvertisersTable)

    var advertiserId: String by FollowAdvertisersTable.advertiserId
    var influencerId: String by FollowAdvertisersTable.influencerId
    var followStatus: FollowStatus by FollowAdvertisersTable.followStatus
}
