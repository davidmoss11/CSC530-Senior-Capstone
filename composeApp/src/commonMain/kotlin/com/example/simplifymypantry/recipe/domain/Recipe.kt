package com.example.simplifymypantry.recipe.domain

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String = "",
    val name: String = "",
    val ingredients: List<String> = emptyList(),
    val instructions: String = "",
    val category: String = "",
    val isPublic: Boolean = false,
    val author: String = "",
    val comments: List<Comment> = emptyList(),
    val isSaved: Boolean = false
)

@Serializable
data class Comment(
    val user: String,
    val text: String,
    val rating: Int
)
