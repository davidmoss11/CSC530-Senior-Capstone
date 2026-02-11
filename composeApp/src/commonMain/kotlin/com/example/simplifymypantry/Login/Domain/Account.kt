package com.example.simplifymypantry.Login.Domain

data class Account(
    val username: String,
    val email: String,
    val password: String,
    val loggedIn: Boolean

)