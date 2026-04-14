package com.example.simplifymypantry.config

import com.example.simplifymypantry.util.SimpleJWT
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


fun AuthenticationConfig.jwtConfig(simpleJWT: SimpleJWT){

    jwt("default"){
        authSchemes("Bearer")
        verifier(simpleJWT.verifier)
        validate { credential ->
            val id = credential.payload.getClaim("id")?.asString()
            println("DEBUG - Validating JWT, ID: $id")

            if (id != null) {
                JWTPrincipal(credential.payload)  // Return JWTPrincipal, not UserIdPrincipal
            } else {
                null
            }
        }
    }
}