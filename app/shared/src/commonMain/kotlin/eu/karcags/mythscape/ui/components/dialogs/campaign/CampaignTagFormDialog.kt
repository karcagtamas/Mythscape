package eu.karcags.mythscape.ui.components.dialogs.campaign

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.dtos.campaigns.CampaignTagDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignTagRequestDTO
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.ui.components.common.AppTextField

@Composable
fun CampaignTagFormDialog(
    onDismiss: () -> Unit,
    onConfirm: (CampaignTagRequestDTO) -> Unit,
    modifier: Modifier = Modifier,
    tag: CampaignTagDTO? = null,
    isProcessing: Boolean = false,
    errorMessage: String? = null,
) {
    val isEdit = tag != null

    var caption by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(tag) {
        if (isEdit) {
            caption = tag.caption
            color = tag.color
        } else {
            caption = ""
            color = "#FFFFFF"
        }
        validationError = null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .width(360.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium),
        title = {
            Text(
                text = if (isEdit) "Edit session" else "Create session",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AppTextField(
                    value = caption,
                    onValueChange = { caption = it; validationError = null },
                    label = "Caption",
                )

                AppTextField(
                    value = color,
                    onValueChange = { color = it; validationError = null },
                    label = "Color"
                )

                val activeError = validationError ?: errorMessage

                activeError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 2.dp, top = 2.dp),
                    )
                }
            }
        },
        confirmButton = {
            Box(modifier = Modifier.width(84.dp)) {
                AppButton(
                    text = "Save",
                    isLoading = isProcessing,
                    enabled = caption.isNotBlank() && color.isNotBlank(),
                    onClick = {
                        try {
                            onConfirm(
                                CampaignTagRequestDTO(
                                    caption = caption,
                                    color = color,
                                )
                            )
                        } catch (_: Exception) {
                            validationError = "Invalid Date or Time syntax"
                        }
                    }
                )
            }
        },
        dismissButton = {
            Box(modifier = Modifier.width(84.dp)) {
                AppButton(
                    text = "Cancel",
                    onClick = { onDismiss() },
                )
            }
        },
    )
}