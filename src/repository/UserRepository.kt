package com.caiomessias.repository

import com.caiomessias.dao.NewUserDao
import com.caiomessias.dao.UserDao
import com.caiomessias.models.UserModel
import com.caiomessias.services.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

interface UserRepository {
    suspend fun getById(id: Long): UserDao?
    suspend fun createUser(newUserDao: NewUserDao): UserDao?
}