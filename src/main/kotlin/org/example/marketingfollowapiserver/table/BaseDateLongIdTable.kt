package org.example.marketingfollowapiserver.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

abstract class BaseDateLongIdTable(tableName: String, idName: String = "id"): LongIdTable(tableName, idName) {
    val createdAt: Column<Long> = long("created_at").clientDefault { System.currentTimeMillis() }
    val lastModifiedAt: Column<Long> = long("last_modified_at").clientDefault { System.currentTimeMillis() }
}
