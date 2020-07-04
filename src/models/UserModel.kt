package com.caiomessias.models

import org.jetbrains.exposed.sql.Table

object UserModel : Table() {
    val id = long("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val name = varchar("name", 255)
    val isEnabled = bool("is_enabled")
}
