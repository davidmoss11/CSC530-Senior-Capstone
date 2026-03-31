package com.example.simplifymypantry

import io.ktor.server.auth.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.simplifymypantry.config.*
import com.example.simplifymypantry.service.*
import com.example.simplifymypantry.database.*
import com.example.simplifymypantry.util.SimpleJWT
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.ktor.plugin.koin
import org.koin.ktor.ext.inject
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {

    install(StatusPages) {
        statusPages()
    }
    install(ContentNegotiation){

    }

    val serviceKoinModule = module {
        single<IAuthService> { AuthService(get()) }
    }

    val databaseKoinModule = module {
        single<IDatabaseFactory> { DatabaseFactory(environment) }
    }

    install(Koin) {
        modules(
            serviceKoinModule,
            databaseKoinModule
        )
    }

    val simpleJWT = SimpleJWT(secret = "test-secret")

    //val simpleJWT = SimpleJWT(secret = environment.config.property("jwt.secret").getString())

    install(Authentication) {
        jwtConfig(simpleJWT)
    }

    routing {
        api(simpleJWT)
    }
}

