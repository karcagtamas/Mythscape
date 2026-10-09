package eu.karcags.mythscape.ui.components.common.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun Avatar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    size: Int = 32,
    content: @Composable () -> Unit,
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface

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
        content()
    }
}