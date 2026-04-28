package com.example.simplifymypantry.api

import com.example.simplifymypantry.models.LoginUser
import com.example.simplifymypantry.models.RegisterUser
import com.example.simplifymypantry.models.UpdateUser
import com.example.simplifymypantry.models.UserResponse
import com.example.simplifymypantry.service.IAuthService
import com.example.simplifymypantry.util.LoginFailed
import com.example.simplifymypantry.util.LoginRateLimiter
import com.example.simplifymypantry.util.SimpleJWT
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.request.receive
import io.ktor.server.response.respond

//Logic for the actual endpoints getting referenced from /api/<endpoint>
//use call.receive to get info from front end, and call.respond to send info back
//call on services to handle db queries

fun Route.auth(authService: IAuthService, simpleJWT: SimpleJWT){

    post("/users") { //registers a new user
        println("/api/users hit")
        val request = call.receive<RegisterUser>()
        val user = authService.createUser(request)
        call.respond(UserResponse.fromUser(user, token = simpleJWT.sign(user.id)))
    }


    post("/login") {
        val request = call.receive<LoginUser>()
        val identifier = request.user.username

        // check if locked out
        if (LoginRateLimiter.isLockedOut(identifier)) {
            val remainingMs = LoginRateLimiter.remainingLockoutMs(identifier)
            val remainingMinutes = remainingMs / 1000 / 60
            call.respond(
                HttpStatusCode.TooManyRequests,
                "Too many failed attempts. Try again in $remainingMinutes minutes."
            )
            return@post
        }

        try {
            val user = authService.getUser(request)
            LoginRateLimiter.clearAttempts(identifier) // clear on success
            call.respond(HttpStatusCode.OK, UserResponse.fromUser(user, token = simpleJWT.sign(user.id)))
        } catch (e: LoginFailed) {
            LoginRateLimiter.recordFailedAttempt(identifier)
            throw e // let StatusPages handle the response
        }
    }
    authenticate("default") {

        get("/user"){ //gets user info once authenticated
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asString()
            val user = authService.getUserById(userId)
            call.respond(UserResponse.fromUser(user, token = simpleJWT.sign(user.id)))
        }

        put("/user"){ //edits user info once authenticated
            val request = call.receive<UpdateUser>()
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asString()
            val user = authService.editUser(userId, request)
            call.respond(UserResponse.fromUser(user, token = simpleJWT.sign(user.id)))
        }

        delete("/user"){ //deletes authorized user
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asString()
            authService.deleteUser(userId)
            call.respond(HttpStatusCode.OK, mapOf("message" to "User deleted successfully"))
        }
    }
}