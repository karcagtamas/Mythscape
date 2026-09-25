package eu.karcags.mythscape.ui.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.ui.components.common.AppButton

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String = "Confirm",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isProcessing: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp,
            )
        },
        confirmButton = {
            Box(modifier = Modifier.width(80.dp)) {
                AppButton(text = confirmText, onClick = onConfirm, isLoading = isProcessing)
            }
        },
        dismissButton = {
            Box(modifier = Modifier.width(80.dp)) {
                AppButton(text = "Cancel", onClick = onDismiss)
            }
        }
    )
}