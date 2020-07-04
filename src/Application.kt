package com.caiomessias

import com.caiomessias.repository.UserRepository
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
import io.ktor.server.netty.EngineMain
import io.ktor.server.netty.Netty

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            jackson {
                propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            }
        }

        DatabaseFactory.init()
        val userRepository = UserRepository()
        val userService = UserService(userRepository)

        routing {
            userRoutes(userService)
        }
    }.start(wait = true)
}

