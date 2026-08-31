package eu.karcags.mythscape.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val AppColors = darkColorScheme(
    primary = Color(0xFFFDBD79),
    onPrimary = Color(0xFF0B0C10),

    secondary = Color(0xFF64B5F6),
    onSecondary = Color(0xFF0B0C10),

    background = Color(0xFF0B0C10),
    onBackground = Color(0xFFF2F2F2),

    surface = Color(0xFF1F2833),
    onSurface = Color(0xFFFFFFFF),

    surfaceVariant = Color(0xFF2C3540),
    onSurfaceVariant = Color(0xFFC4C7C5),

    error = Color(0xFF9F0000),
    onError = Color(0xFFFFFFFF),

    outline = Color(0xFF455161),
)

val ShareShapes = Shapes(
    small = RoundedCornerShape(2.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(4.dp),
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        shapes = ShareShapes,
        content = content,
    )
}