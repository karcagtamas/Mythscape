package eu.karcags.mythscape.ui.components.common.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eu.karcags.mythscape.utils.parseHexOrNull
import eu.karcags.mythscape.utils.toHexString

@Composable
fun ColorField(
    color: Color?,
    onColorChange: (Color?) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    var hexText by remember(color) { mutableStateOf(color?.toHexString() ?: "") }
    var localErrorMessage by remember { mutableStateOf<String?>(null) }

    AppTextField(
        value = hexText,
        onValueChange = { input ->
            val sanitized = input.uppercase().filter { it in "0123456789ABCDEF#" }
            if (sanitized.length <= 9) {
                hexText = sanitized
                val parsed = sanitized.parseHexOrNull()
                if (parsed != null) {
                    localErrorMessage = null
                    onColorChange(parsed)
                } else {
                    if (sanitized.isNotEmpty()) {
                        localErrorMessage = "Invalid color format"
                    }
                }
            }
        },
        label = label,
        errorMessage = errorMessage,
        leading = {
            color?.let {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(it)
                        .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium),
                )
            }
        },
        modifier = modifier,
    )
}