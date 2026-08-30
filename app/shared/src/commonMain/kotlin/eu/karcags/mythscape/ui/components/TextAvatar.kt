package eu.karcags.mythscape.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextAvatar(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    size: Int = 32,
) {
    val glyph = remember(text) {
        if (text.length >= 2) text.take(2).uppercase() else text.uppercase()
    }

    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val borderModifier = if (isSelected) {
        Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
    } else Modifier

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size.dp)
            .then(borderModifier)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() },
    ) {
        Text(
            text = glyph,
            color = textColor,
            fontSize = if (size > 30) 12.sp else 10.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}