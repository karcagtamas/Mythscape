package eu.karcags.mythscape.ui.components.dialogs.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.karcags.mythscape.dtos.campaigns.CampaignMemberDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignMemberRequestDTO
import eu.karcags.mythscape.ui.components.common.fields.AppTextField
import eu.karcags.mythscape.ui.components.dialogs.EditDialog

@Composable
fun CampaignMemberFormDialog(
    onDismiss: () -> Unit,
    onConfirm: (CampaignMemberRequestDTO) -> Unit,
    modifier: Modifier = Modifier,
    member: CampaignMemberDTO? = null,
    isProcessing: Boolean = false,
    errorMessage: String? = null,
) {
    val isEdit = member != null

    var name by remember { mutableStateOf("") }

    val nameError by remember {
        derivedStateOf {
            when {
                name.isBlank() -> "Name is required"
                name.length >= 40 -> "Caption has to be maximum 40 characters"
                else -> null
            }
        }
    }

    LaunchedEffect(member) {
        if (isEdit) {
            name = member.name
        } else {
            name = ""
        }
    }

    EditDialog(
        onDismiss = onDismiss,
        onOk = {
            onConfirm(
                CampaignMemberRequestDTO(
                    name = name,
                    userId = null,
                )
            )
        },
        title = if (isEdit) "Edit Campaign Member" else "Create Campaign Member",
        modifier = modifier
            .width(400.dp),
        okText = if (isEdit) "Edit" else "Create",
        isProcessing = isProcessing,
        okEnabled = nameError == null,
        errorMessage = errorMessage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppTextField(
                value = name,
                onValueChange = { name = it },
                label = "Name",
                errorMessage = nameError,
            )
        }
    }
}