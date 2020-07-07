package com.caiomessias.dao

import org.valiktor.functions.hasSize
import org.valiktor.validate

data class UserDao(val id: Long, val name: String, val isEnabled: Boolean)

data class NewUserDao(val name: String, val isEnabled: Boolean) {
    init {
        validate(this) {
            validate(NewUserDao::name).hasSize(min = 3, max = 128)
        }
    }
}