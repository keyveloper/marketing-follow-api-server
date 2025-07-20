package org.example.marketingfollowapiserver.config

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.example.marketingfollowapiserver.table.FollowAdvertisersTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class SchemaInitializer {
    private val logger = KotlinLogging.logger {}

    @PostConstruct
    fun init() {
        logger.info { "Initializing database schema..." }
        transaction {
            SchemaUtils.create(FollowAdvertisersTable)
        }
        logger.info { "Database schema initialization completed" }
    }
}
