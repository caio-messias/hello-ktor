package com.caiomessias.routes

import com.caiomessias.dao.NewUserDao
import com.caiomessias.services.UserService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.userRoutes(userService: UserService) {
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
        post<NewUserDao>("") { userDao ->
            val user = userService.createUser(userDao)
            if (user != null) {
                call.respond(HttpStatusCode.Created)
            } else {
                call.respond(HttpStatusCode.Conflict)
            }
        }
    }
}


