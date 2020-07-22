package com.caiomessias.repository

import com.caiomessias.dao.NewUserDao
import com.caiomessias.dao.UserDao

interface UserRepository {
    suspend fun getById(id: Long): UserDao?
    suspend fun createUser(newUserDao: NewUserDao): UserDao?
}