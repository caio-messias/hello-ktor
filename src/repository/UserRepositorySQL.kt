package com.caiomessias.repository

import com.caiomessias.dao.NewUserDao
import com.caiomessias.dao.UserDao
import com.caiomessias.models.UserModel
import com.caiomessias.services.DatabaseFactory
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepositorySQL : UserRepository {
    override suspend fun getById(id: Long): UserDao? {
        return DatabaseFactory.dbQuery {
            UserModel.select { UserModel.id eq id }
                .mapNotNull { toUserDao(it) }
                .singleOrNull()
        }
    }

    override suspend fun createUser(newUserDao: NewUserDao): UserDao? {
        var key = 0L
        DatabaseFactory.dbQuery {
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