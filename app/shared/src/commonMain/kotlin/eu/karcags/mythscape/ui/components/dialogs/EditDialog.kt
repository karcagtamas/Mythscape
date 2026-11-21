package eu.karcags.mythscape.ui.components.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.enums.ColorVariant
import eu.karcags.mythscape.ui.components.common.AppButton

@Composable
fun EditDialog(
    onDismiss: () -> Unit,
    onOk: () -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    okText: String = "Ok",
    cancelText: String = "Cancel",
    isProcessing: Boolean = false,
    okEnabled: Boolean = true,
    errorMessage: String? = null,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium),
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            content()

            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        },
        confirmButton = {
            AppButton(
                text = okText,
                isLoading = isProcessing,
                enabled = okEnabled,
                onClick = onOk,
                modifier = Modifier
                    .width(80.dp),
            )
        },
        dismissButton = {
            AppButton(
                text = cancelText,
                color = ColorVariant.Outline,
                onClick = onDismiss,
                modifier = Modifier
                    .width(80.dp),
            )
        },
    )
}