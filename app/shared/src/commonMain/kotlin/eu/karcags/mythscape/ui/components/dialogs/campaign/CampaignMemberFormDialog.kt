package eu.karcags.mythscape.ui.components.dialogs.campaign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.karcags.mythscape.core.dtos.UserDTO
import eu.karcags.mythscape.core.dtos.campaigns.CampaignMemberDTO
import eu.karcags.mythscape.core.dtos.campaigns.CampaignMemberRequestDTO
import eu.karcags.mythscape.ui.components.common.fields.AppTextField
import eu.karcags.mythscape.ui.components.common.fields.UserField
import eu.karcags.mythscape.ui.components.dialogs.EditDialog

@Composable
fun CampaignMemberFormDialog(
    onDismiss: () -> Unit,
    onConfirm: (CampaignMemberRequestDTO) -> Unit,
    modifier: Modifier = Modifier,
    member: CampaignMemberDTO? = null,
    users: List<UserDTO> = emptyList(),
    isProcessing: Boolean = false,
    errorMessage: String? = null,
) {
    val isEdit = member != null

    var name by remember { mutableStateOf("") }
    var user by remember { mutableStateOf<UserDTO?>(null) }

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
            user = member.user
        } else {
            name = ""
            user = null
        }
    }

    EditDialog(
        onDismiss = onDismiss,
        onOk = {
            onConfirm(
                CampaignMemberRequestDTO(
                    name = name,
                    userId = user?.id,
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
                readOnly = user != null
            )

            UserField(
                selectedUser = user,
                users = users,
                onSelect = {
                    name = it?.name ?: ""
                    user = it
                },
                label = "User",
                clearable = true,
            )
        }
    }
}