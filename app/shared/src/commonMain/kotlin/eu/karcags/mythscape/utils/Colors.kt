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

fun Color.toHexString(): String {
    val r = (red * 255).toInt().coerceIn(0, 255)
    val g = (green * 255).toInt().coerceIn(0, 255)
    val b = (blue * 255).toInt().coerceIn(0, 255)
    return "#" + listOf(r, g, b).joinToString("") {
        it.toString(16).padStart(2, '0').uppercase()
    }
}

fun String.parseHexOrNull(): Color? {
    val cleanHex = this.removePrefix("#")
    if (cleanHex.length != 6 && cleanHex.length != 8) return null
    val colorLong = cleanHex.toLongOrNull(16) ?: return null
    return Color(if (cleanHex.length == 6) colorLong or 0xFF000000L else colorLong)
}