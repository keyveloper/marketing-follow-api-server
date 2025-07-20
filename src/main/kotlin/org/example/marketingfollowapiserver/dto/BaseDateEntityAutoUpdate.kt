package org.example.marketingfollowapiserver.dto

import io.github.oshai.kotlinlogging.KotlinLogging
import org.example.marketingfollowapiserver.table.BaseDateLongIdTable
import org.jetbrains.exposed.dao.EntityChangeType
import org.jetbrains.exposed.dao.EntityHook
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.toEntity

abstract class BaseDateEntityAutoUpdate<E: BaseDateEntity>(table: BaseDateLongIdTable): LongEntityClass<E>(table) {
    private val logger = KotlinLogging.logger {}
    init {
        EntityHook.subscribe { action ->
            if (action.changeType == EntityChangeType.Updated) {
                try {
                    action.toEntity(this)?.lastModifiedAt = System.currentTimeMillis()
                } catch (e: Exception) {
                    logger.warn { "Failed to update entity $this updatedAt" }
                }
            }
        }
    }
}
