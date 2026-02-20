package com.example.simplifymypantry.login.domain

data class Account(
    val username: String,
    val email: String,
    val password: String,
    val loggedIn: Boolean

)