package com.example.simplifymypantry.core

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme

// Solid high-contrast palette
val PantryGreen = Color(0xFF1B5E20)      // Deep Forest Green
val PantryGreenMedium = Color(0xFF2E7D32) // Medium Green for buttons
val PantryGreenLight = Color(0xFFE8F5E9) // Light Mint for card backgrounds
val PantryWhite = Color(0xFFFFFFFF)      // Pure White
val PantryBlack = Color(0xFF000000)      // Pure Black for text
val PantryError = Color(0xFFB00020)      // High-visibility Red

val LightColors = lightColorScheme(
    primary = PantryGreen,
    onPrimary = PantryWhite,
    secondary = PantryGreenMedium,
    onSecondary = PantryWhite,
    background = PantryWhite,
    onBackground = PantryBlack,
    surface = PantryWhite,
    onSurface = PantryBlack,
    surfaceVariant = PantryGreenLight,
    onSurfaceVariant = PantryGreen,
    error = PantryError,
    onError = PantryWhite,
    outline = PantryGreen
)
