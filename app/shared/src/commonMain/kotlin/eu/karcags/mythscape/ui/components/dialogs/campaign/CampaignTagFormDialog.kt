package eu.karcags.mythscape.ui.components.dialogs.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.karcags.mythscape.dtos.campaigns.CampaignTagDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignTagRequestDTO
import eu.karcags.mythscape.ui.components.common.fields.AppTextField
import eu.karcags.mythscape.ui.components.common.fields.ColorField
import eu.karcags.mythscape.ui.components.dialogs.EditDialog
import eu.karcags.mythscape.utils.parseHexOrNull
import eu.karcags.mythscape.utils.toHexString

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
    var color by remember { mutableStateOf("#FFFFFF".parseHexOrNull()) }

    val captionError by remember {
        derivedStateOf {
            when {
                caption.isBlank() -> "Caption is required"
                caption.length >= 40 -> "Caption has to be at least 40 characters"
                else -> null
            }
        }
    }

    val colorError by remember {
        derivedStateOf {
            when {
                color == null -> "Caption is required"
                else -> null
            }
        }
    }

    LaunchedEffect(tag) {
        if (isEdit) {
            caption = tag.caption
            color = tag.color.parseHexOrNull()
        } else {
            caption = ""
            color = "#FFFFFF".parseHexOrNull()
        }
    }

    EditDialog(
        onDismiss = onDismiss,
        onOk = {
            onConfirm(
                CampaignTagRequestDTO(
                    caption = caption,
                    color = color?.toHexString()!!,
                )
            )
        },
        title = if (isEdit) "Edit Campaign Tag" else "Create Campaign Tag",
        modifier = modifier
            .width(400.dp),
        okText = if (isEdit) "Edit" else "Create",
        isProcessing = isProcessing,
        okEnabled = captionError == null && colorError == null,
        errorMessage = errorMessage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppTextField(
                value = caption,
                onValueChange = { caption = it },
                label = "Caption",
                errorMessage = captionError,
            )
            ColorField(
                color = color,
                onColorChange = { color = it },
                label = "Color",
                errorMessage = colorError,
            )
        }
    }
}