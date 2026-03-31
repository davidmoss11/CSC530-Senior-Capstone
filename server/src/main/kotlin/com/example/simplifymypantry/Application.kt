package com.example.simplifymypantry

import io.ktor.server.auth.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.simplifymypantry.config.*
import com.example.simplifymypantry.util.SimpleJWT
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {


    install(Authentication)
    install(StatusPages) {
        statusPages()
    }
    install(ContentNegotiation){

    }

    val simpleJWT = SimpleJWT(secret = environment.config.property("jwt.secret").getString())

    routing {
        api(simpleJWT)
    }
}

