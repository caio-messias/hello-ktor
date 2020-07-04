package com.caiomessias.repository

import com.caiomessias.dao.NewUserDao
import com.caiomessias.dao.UserDao
import com.caiomessias.models.UserModel
import com.caiomessias.services.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepository {
    suspend fun getById(id: Long): UserDao? {
        return dbQuery {
            UserModel.select { UserModel.id eq id }
                .mapNotNull { toUserDao(it) }
                .singleOrNull()
        }
    }

    suspend fun createUser(newUserDao: NewUserDao): UserDao? {
        var key = 0L
        dbQuery {
            key = (UserModel.insert {
                it[name] = newUserDao.name
                it[isEnabled] = newUserDao.isEnabled
            } get UserModel.id)
        }
        return getById(key)
    }

    private fun toUserDao(row: ResultRow): UserDao {
        return UserDao(
            row[UserModel.id],
            row[UserModel.name],
            row[UserModel.isEnabled]
        )
    }
}