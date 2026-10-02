package eu.karcags.mythscape.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.enums.ColorVariant
import eu.karcags.mythscape.enums.getColors
import mythscape.app.shared.generated.resources.Res
import mythscape.app.shared.generated.resources.email_24

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    color: ColorVariant = ColorVariant.Default,
    icon: @Composable (() -> Unit)? = null,
) {
    val (containerColor, textColor) = color.getColors()

    Box(
        modifier = modifier,
    ) {
        Button(
            onClick = onClick,
            enabled = enabled && !isLoading,
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(vertical = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                disabledContainerColor = containerColor.copy(alpha = 0.4f),
                contentColor = textColor,
                disabledContentColor = textColor.copy(alpha = 0.4f),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = textColor,
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                )
            } else {
                icon?.let {
                    icon.invoke()
                    Spacer(modifier = Modifier.width(2.dp))
                }
                Text(
                    text = text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                )

            }
        }
    }
}