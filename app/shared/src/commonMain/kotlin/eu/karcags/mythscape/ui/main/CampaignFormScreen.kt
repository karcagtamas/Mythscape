package eu.karcags.mythscape.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.enums.FormState
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.ui.components.common.AppTextField
import eu.karcags.mythscape.ui.components.common.PrimaryCard
import eu.karcags.mythscape.viewmodel.CampaignFormViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CampaignFormScreen(
    mode: FormState,
    onCancel: () -> Unit,
    onComplete: (CampaignDTO) -> Unit,
    campaign: CampaignDTO? = null,
    viewModel: CampaignFormViewModel = koinViewModel(),
) {
    LaunchedEffect(campaign?.id, mode) {
        viewModel.setup(mode, campaign)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        contentAlignment = Alignment.TopStart,
    ) {
        PrimaryCard(
            title = if (viewModel.mode == FormState.EDIT) "Edit Campaign" else "Create Campaign",
        ) {
            AppTextField(
                label = "Name",
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
            )

            AppTextField(
                label = "Title",
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
            )

            AppTextField(
                label = "Description",
                value = viewModel.description ?: "",
                onValueChange = {
                    viewModel.description = it.ifBlank { null }
                },
            )

            viewModel.fieldError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 11.sp,
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    AppButton(text = "Cancel", onClick = onCancel)
                }
                Box(modifier = Modifier.weight(1f)) {
                    AppButton(
                        text = if (viewModel.mode == FormState.EDIT) "Save Changes" else "Create",
                        isLoading = viewModel.isProcessing,
                        onClick = {
                            viewModel.submit { campaign ->
                                onComplete(campaign)
                            }
                        }
                    )
                }
            }
        }
    }
}