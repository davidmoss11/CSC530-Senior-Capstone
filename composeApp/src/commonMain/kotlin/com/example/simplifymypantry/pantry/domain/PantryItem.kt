package com.example.simplifymypantry.pantry.domain

import kotlinx.serialization.Serializable

@Serializable
data class PantryItem(
    val id: String = "",
    val name: String,
    val quantity: String = "",
    val category: String = "",
    val expirationDate: String = "",
    val dietInfo: String = "",
    val notes: String = ""
)
