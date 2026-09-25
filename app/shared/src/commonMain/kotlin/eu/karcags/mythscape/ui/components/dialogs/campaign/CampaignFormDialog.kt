package eu.karcags.mythscape.ui.components.dialogs.campaign

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.dtos.campaigns.CampaignRequestDTO
import eu.karcags.mythscape.enums.FormState
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.ui.components.common.AppTextField

@Composable
fun CampaignFormDialog(
    mode: FormState,
    onDismiss: () -> Unit,
    onConfirm: (CampaignRequestDTO) -> Unit,
    modifier: Modifier = Modifier,
    campaign: CampaignDTO? = null,
    isProcessing: Boolean = false,
    errorMessage: String? = null,
) {
    var name by remember { mutableStateOf(if (mode == FormState.EDIT) campaign?.name ?: "" else "") }
    var title by remember { mutableStateOf(if (mode == FormState.EDIT) campaign?.title ?: "" else "") }
    var description by remember { mutableStateOf(if (mode == FormState.EDIT) campaign?.description ?: "" else "") }

    LaunchedEffect(campaign?.id, mode) {
        if (mode == FormState.EDIT && campaign != null) {
            name = campaign.name
            title = campaign.title
            description = campaign.description ?: ""
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .width(400.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium),
        title = {
            Text(
                text = if (mode == FormState.CREATE) "Create Campaign" else "Edit Campaign",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        },
        text = {
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

                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
        },
        confirmButton = {
            Box(modifier = Modifier.width(84.dp)) {
                AppButton(
                    text = if (mode == FormState.CREATE) "Forge" else "Save",
                    isLoading = isProcessing,
                    enabled = name.isNotBlank() && title.isNotBlank(),
                    onClick = {
                        onConfirm(
                            CampaignRequestDTO(
                                name = name,
                                title = title,
                                description = description.ifBlank { null }
                            )
                        )
                    }
                )
            }
        },
        dismissButton = {
            Box(modifier = Modifier.width(84.dp)) {
                AppButton(
                    text = "Cancel",
                    onClick = onDismiss
                )
            }
        },
    )
}