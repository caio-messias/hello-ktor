package com.caiomessias.routes

import com.caiomessias.dao.NewUserDao
import com.caiomessias.errors.InputValidationError
import com.caiomessias.services.UserService
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage

fun Route.userRoutes(userService: UserService) {
    install(StatusPages) {
        exception<RuntimeException> { e ->
            call.respond(HttpStatusCode.BadRequest, e.message ?: "")
        }
    }

    route("/users") {
        // $ curl -i "localhost:8080/users/1"
        get("/{id}") {
            val id = call.parameters["id"]?.toLong() ?: 0
            val user = userService.getById(id)

            if (user != null) {
                call.respond(user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // $ curl -i -X POST -H "Content-Type: application/json" -d '{"name":"aaa","is_enabled":"true"}' "localhost:8080/users"
        post("") {
            try {
                val userDao = call.receive<NewUserDao>()
                val user = userService.createUser(userDao)

                if (user != null) {
                    call.respond(HttpStatusCode.Created)
                } else {
                    call.respond(HttpStatusCode.Conflict)
                }
            } catch (e: ValueInstantiationException) {
                val cause = e.cause
                if (cause is ConstraintViolationException) {
                    val violations = cause.constraintViolations
                        .mapToMessage(baseName = "messages")
                        .map { InputValidationError(it.property, it.message) }

                    call.respond(HttpStatusCode.BadRequest, violations)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}
