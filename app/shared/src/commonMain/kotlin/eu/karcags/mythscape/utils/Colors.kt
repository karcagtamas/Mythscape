package eu.karcags.mythscape.utils

import androidx.compose.ui.graphics.Color

fun Color.Companion.fromHex(hex: String): Color {
    val cleanHex = hex.removePrefix("#")
    val colorLong = when (cleanHex.length) {
        6 -> cleanHex.toLong(16) or 0xFF000000L
        8 -> cleanHex.toLong(16)
        else -> throw IllegalArgumentException("Invalid hex color: $hex")
    }
    return Color(colorLong)
}