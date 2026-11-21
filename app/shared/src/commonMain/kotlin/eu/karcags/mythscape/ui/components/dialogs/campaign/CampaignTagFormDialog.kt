package eu.karcags.mythscape.ui.components.dialogs.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.karcags.mythscape.dtos.campaigns.CampaignTagDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignTagRequestDTO
import eu.karcags.mythscape.ui.components.common.AppTextField
import eu.karcags.mythscape.ui.components.dialogs.EditDialog

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
    var color by remember { mutableStateOf("#FFFFFF") }
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

    EditDialog(
        onDismiss = onDismiss,
        onOk = {
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
        },
        title = if (isEdit) "Edit Campaign Tag" else "Create Campaign Tag",
        modifier = modifier
            .width(400.dp),
        okText = if (isEdit) "Edit" else "Create",
        isProcessing = isProcessing,
        okEnabled = caption.isNotBlank() && color.isNotBlank(),
        errorMessage = validationError ?: errorMessage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
        }
    }
}