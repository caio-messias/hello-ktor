package com.caiomessias

import com.caiomessias.repository.UserRepositorySQL
import com.caiomessias.routes.userRoutes
import com.caiomessias.services.DatabaseFactory
import com.caiomessias.services.UserService
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8800) {
        DatabaseFactory.init()
        userRoutesImpl()
    }.start(wait = true)
}

fun Application.userRoutesImpl() {
    val userRepository = UserRepositorySQL()
    val userService = UserService(userRepository)

    userRoutesComponent(userService)
}

fun Application.userRoutesComponent(userService: UserService) {
    install(ContentNegotiation) {
        jackson {
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        }
    }

    routing {
        userRoutes(userService)
    }
}