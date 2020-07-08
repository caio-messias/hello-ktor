package com.caiomessias.services

import com.caiomessias.dao.NewUserDao
import com.caiomessias.dao.UserDao
import com.caiomessias.repository.UserRepository

class UserService(private val userRepository: UserRepository?) {
    suspend fun getById(id: Long): UserDao? {
        return userRepository?.getById(id)
    }

    suspend fun createUser(newUserDao: NewUserDao): UserDao? {
        return userRepository?.createUser(newUserDao)
    }
}