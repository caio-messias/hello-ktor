package com.caiomessias.dao

data class UserDao(val id: Long, val name: String, val isEnabled: Boolean)
data class NewUserDao(val name: String, val isEnabled: Boolean)