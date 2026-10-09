package eu.karcags.mythscape.ui.components.common.avatar

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconAvatar(
    icon: DrawableResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    size: Int = 32,
) {
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Avatar(
        onClick = onClick,
        modifier = modifier,
        isSelected = isSelected,
        size = size,
    ) {
        Icon(
            painter = painterResource(icon),
            tint = textColor,
            contentDescription = null,
        )
    }
}