package com.example.simplifymypantry.config

import com.example.simplifymypantry.api.auth
import com.example.simplifymypantry.service.*
import com.example.simplifymypantry.util.SimpleJWT
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Routing.api(simpleJWT: SimpleJWT) {

    val authService: IAuthService by inject() //creates authService only when called, lazy

    route("/api") { //all api endpoints start at /api

        get {
            call.respond("Welcome to Simplify My Pantry's API")
        }

        apply {
            auth(authService, simpleJWT)
        }
        //routes all user routes to auth, and passes in authService functions to call
    }

}

