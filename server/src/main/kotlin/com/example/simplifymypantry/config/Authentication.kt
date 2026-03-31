package com.example.simplifymypantry.config

import com.example.simplifymypantry.util.SimpleJWT
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


fun AuthenticationConfig.jwtConfig(simpleJWT: SimpleJWT){

    jwt{
        authSchemes("Token")
        verifier(simpleJWT.verifier)
        validate { //finds the "id" portion of the JWT payload,
            println(it.payload.getClaim("id").asString())
            UserIdPrincipal(it.payload.getClaim("id").asString())
        }
    }
}