package com.example.simplifymypantry.account.domain

import kotlinx.serialization.Serializable

@Serializable
data class ReturnedAccount(
    val id: String,
    val username: String,
    val email: String,
    val token: String
)

data class SentAccount(
    val username: String,
    val email: String,
    val password: String
)