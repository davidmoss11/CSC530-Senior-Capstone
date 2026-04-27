package com.example.simplifymypantry.scanner.data

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class OpenFoodFactsAPI(){

    val log = Logger.withTag("OPENFOODFACTS API")
    private val client = HttpClient(CIO){
        install(ContentNegotiation){
            json(Json{ ignoreUnknownKeys = true})
        }
    }

    private val base_url = "https://world.openfoodfacts.net/api/v2/"
    private val user_agent = "SimplifyMyPantry/v0.0.1 spritchett8@murraystate.edu"

    suspend fun getProduct(
        barcode: String
    ) : ProductResponse {

        val response = client.get("$base_url/products/$barcode"){
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.UserAgent, user_agent)
            }
            basicAuth("off", "off")
        }.body<ProductResponse>()

        log.w(response.toString())

        return response

    }
}