package com.example.simplifymypantry.models

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object Users: UUIDTable() {

    val username = varchar("username", 255).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
    val hashedPassword = varchar("hashedPassword", 255)
    val isAdmin = bool("isAdmin").default(false)

}

class User(id: EntityID<UUID>) : UUIDEntity(id){
    companion object : UUIDEntityClass<User>(Users)

    var username by Users.username
    var email by Users.email
    var hashedPassword by Users.hashedPassword
    var isAdmin by Users.isAdmin

}

data class RegisterUser(val user: User) {
    data class User(val email: String, val username: String, val password: String)
}

data class LoginUser(val user: User) {
    data class User(val username: String, val email: String, val password: String)
}

data class UpdateUser(val user: User) {
    data class User(
        val email: String? = null,
        val username: String? = null,
        val password: String? = null
    )
}

data class UserResponse(
    val id: String,
    val email: String,
    val username: String,
    val token: String
) {
    companion object {
        fun fromUser(user: User, token: String): UserResponse {
            return UserResponse(
                id = user.id.toString(),
                email = user.email,
                username = user.username,
                token = token
            )
        }
    }
}

