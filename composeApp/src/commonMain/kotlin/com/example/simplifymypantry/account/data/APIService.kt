package com.example.simplifymypantry.account.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.client.engine.cio.CIO
import co.touchlab.kermit.Logger

class AccountApiService{

    val log = Logger.withTag("API Service")

    private val client = HttpClient(CIO){
        install(ContentNegotiation){
            json(Json{ ignoreUnknownKeys = true})
        }
    }

    private val base_url = "http://10.55.82.98:8080/api"

    suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ) : UserResponse{
        log.d("Register User Activated")
        return client.post("$base_url/users"){
            contentType(ContentType.Application.Json)
            setBody(RegisterUserRequest(user = RegisterUserData(username, email, password)))
        }.body()
    }

    suspend fun deleteUser(
        token: String
    ) : Result<String>{
        return try {
            client.delete("$base_url/user") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            Result.success("User deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(
        token: String
    ) : UserResponse{
        return client.get("$base_url/user"){
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    suspend fun loginUser(
        username: String,
        email: String,
        password: String
    ) : UserResponse{
        return client.post("$base_url/login"){
            contentType(ContentType.Application.Json)
            setBody(RegisterUserRequest(user = RegisterUserData(username, email, password)))
        }.body()
    }//I know setBody says "Register", but they do the same function

    suspend fun editUser(
        token: String,
        username: String?,
        email: String?,
        password: String?,
    ) : UserResponse{
        return client.put("$base_url/user"){
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(EditUserRequest(user = EditUserData(username, email, password)))
        }.body()
    }
}