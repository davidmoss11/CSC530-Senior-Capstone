package com.example.simplifymypantry

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform