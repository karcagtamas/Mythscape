package eu.karcags.mythscape.enums

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

enum class ColorVariant {
    Primary,
    Secondary,
    Tertiary,
    Error,
    Background,
    Outline,
    Surface,
    Default,
}

@Composable
fun ColorVariant.getColor() = when (this) {
    ColorVariant.Primary, ColorVariant.Default -> MaterialTheme.colorScheme.primary
    ColorVariant.Secondary -> MaterialTheme.colorScheme.secondary
    ColorVariant.Tertiary -> MaterialTheme.colorScheme.tertiary
    ColorVariant.Error -> MaterialTheme.colorScheme.error
    ColorVariant.Background -> MaterialTheme.colorScheme.background
    ColorVariant.Outline -> MaterialTheme.colorScheme.outline
    ColorVariant.Surface -> MaterialTheme.colorScheme.surface
}

@Composable
fun ColorVariant.getTextColor() = when (this) {
    ColorVariant.Primary, ColorVariant.Default -> MaterialTheme.colorScheme.onPrimary
    ColorVariant.Secondary -> MaterialTheme.colorScheme.onSecondary
    ColorVariant.Tertiary -> MaterialTheme.colorScheme.onTertiary
    ColorVariant.Error -> MaterialTheme.colorScheme.onError
    ColorVariant.Background -> MaterialTheme.colorScheme.background
    ColorVariant.Outline -> MaterialTheme.colorScheme.outline
    ColorVariant.Surface -> MaterialTheme.colorScheme.surface
}

@Composable
fun ColorVariant.getColors() = Pair(getColor(), getTextColor())