package com.caiomessias.routes

import com.caiomessias.dao.NewUserDao
import com.caiomessias.errors.InputValidationError
import com.caiomessias.services.UserService
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage

fun Route.userRoutes(userService: UserService) {
    route("/users") {
        // $ curl -i "localhost:8080/users/1"
        get("/{id}") {
            val id = call.parameters["id"]?.toLong() ?: 0
            val user = userService.getById(id)

            user?.let {
                call.respond(user)
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        // $ curl -i -X POST -H "Content-Type: application/json" -d '{"name":"aaa","is_enabled":"true"}' "localhost:8080/users"
        post("") {
            try {
                val newUser = call.receive<NewUserDao>()
                val user = userService.createUser(newUser)

                user?.let {
                    call.response.header("Location", "/users/${user.id}")
                    call.respond(HttpStatusCode.Created, user)
                } ?: call.respond(HttpStatusCode.Conflict)
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
