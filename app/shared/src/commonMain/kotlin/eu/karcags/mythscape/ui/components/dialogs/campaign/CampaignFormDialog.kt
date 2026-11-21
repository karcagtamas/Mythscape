package eu.karcags.mythscape.ui.components.dialogs.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignRequestDTO
import eu.karcags.mythscape.ui.components.common.fields.AppTextField
import eu.karcags.mythscape.ui.components.dialogs.EditDialog

@Composable
fun CampaignFormDialog(
    onDismiss: () -> Unit,
    onConfirm: (CampaignRequestDTO) -> Unit,
    modifier: Modifier = Modifier,
    campaign: CampaignDTO? = null,
    isProcessing: Boolean = false,
    errorMessage: String? = null,
) {
    var isEdit by remember { mutableStateOf(campaign != null) }
    var name by remember { mutableStateOf(campaign?.name ?: "") }
    var title by remember { mutableStateOf(campaign?.title ?: "") }
    var description by remember { mutableStateOf(campaign?.description ?: "") }

    LaunchedEffect(campaign?.id) {
        isEdit = campaign != null
        campaign?.let {
            name = it.name
            title = it.title
            description = it.description ?: ""
        }
    }

    EditDialog(
        onDismiss = onDismiss,
        onOk = {
            onConfirm(
                CampaignRequestDTO(
                    name = name,
                    title = title,
                    description = description.ifBlank { null }
                )
            )
        },
        title = if (isEdit) "Edit Campaign" else "Create Campaign",
        modifier = modifier
            .width(400.dp),
        okText = if (isEdit) "Edit" else "Forge",
        isProcessing = isProcessing,
        okEnabled = name.isNotBlank() && title.isNotBlank(),
        errorMessage = errorMessage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppTextField(
                value = name,
                onValueChange = { name = it },
                label = "Name (Acronym)"
            )
            AppTextField(
                value = title,
                onValueChange = { title = it },
                label = "Title"
            )
            AppTextField(
                value = description,
                onValueChange = { description = it },
                label = "Campaign Synopsis Summary"
            )
        }
    }
}