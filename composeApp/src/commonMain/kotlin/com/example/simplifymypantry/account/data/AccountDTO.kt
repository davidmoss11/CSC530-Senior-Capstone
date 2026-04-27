package com.example.simplifymypantry.account.data

import com.example.simplifymypantry.account.domain.ReturnedAccount
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val user: RegisterUserData
)

@Serializable
data class EditUserRequest(
    val user: EditUserData
)

@Serializable
data class RegisterUserData(
    val username: String,
    val email: String,
    val password: String
)
@Serializable
data class EditUserData(
    val username: String?,
    val email: String?,
    val password: String?
)

@Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val token: String
)

data class MessageResponse(
    val message: String
)

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    val token: String
)

// Mapper
fun UserResponse.toDomain(): ReturnedAccount {



    return ReturnedAccount(
        id = id,
        username = username,
        email = email,
        token = token
    )
}