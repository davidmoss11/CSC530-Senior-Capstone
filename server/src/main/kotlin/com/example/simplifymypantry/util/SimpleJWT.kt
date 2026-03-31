package com.example.simplifymypantry.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import java.util.UUID

class SimpleJWT(secret: String) {

    private val algorithm = Algorithm.HMAC384(secret)

    val verifier : JWTVerifier = JWT.require(algorithm).build()

    fun sign(id: EntityID<UUID>): String = JWT.create()
        .withClaim("id", id.toString())
        .sign(algorithm)

}