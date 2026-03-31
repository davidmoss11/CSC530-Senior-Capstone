package com.example.simplifymypantry.config

import com.example.simplifymypantry.util.*
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlin.collections.mapOf

//throws different status codes based on whatever error is thrown
fun StatusPagesConfig.statusPages() {
    exception<Throwable> { call, cause ->
        when (cause) {
            is AuthenticationException -> call.respond(HttpStatusCode.Unauthorized)
            is AuthorizationException -> call.respond(HttpStatusCode.Forbidden)
            is ValidationException -> call.respond(HttpStatusCode.UnprocessableEntity)
            is UserExists -> call.respond(
                HttpStatusCode.UnprocessableEntity,
                mapOf("errors" to mapOf("user" to listOf("exists")))
            )
            is UserDoesNotExist -> call.respond(HttpStatusCode.NotFound)
        }
    }
}