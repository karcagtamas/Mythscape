package eu.karcags.mythscape.ui.main.campaign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.dtos.campaigns.CampaignTagDTO
import eu.karcags.mythscape.dtos.sessions.SessionDTO
import eu.karcags.mythscape.enums.ColorVariant
import eu.karcags.mythscape.theme.SmallIconSize
import eu.karcags.mythscape.ui.components.common.*
import eu.karcags.mythscape.ui.components.dialogs.campaign.CampaignTagFormDialog
import eu.karcags.mythscape.utils.fromHex
import eu.karcags.mythscape.viewmodel.campaign.CampaignTagsViewModel
import kotlinx.datetime.LocalDateTime
import mythscape.app.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CampaignTagsScreen(
    campaignId: Int,
    viewModel: CampaignTagsViewModel = koinViewModel(),
) {
    LaunchedEffect(campaignId) {
        viewModel.initialize(campaignId)
    }

    val tags by viewModel.tags.collectAsState()

    Page {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PageHeader(
                "Campaign Tags",
            ) {
                AppButton(
                    text = "Add",
                    modifier = Modifier
                        .width(120.dp),
                    onClick = { viewModel.openCreateDialog() },
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_add_24),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                items(tags) { tag ->
                    TagRow(
                        tag = tag,
                        onEdit = {
                            viewModel.openEditDialog(tag)
                        },
                        onDelete = {
                            viewModel.delete(tag)
                        },
                    )
                }
            }
        }

        if (viewModel.showFormDialog) {
            CampaignTagFormDialog(
                tag = viewModel.editing,
                isProcessing = viewModel.isProcessing,
                onDismiss = { viewModel.showFormDialog = false },
                onConfirm = { viewModel.save(it) },
            )
        }
    }
}

@Composable
private fun TagRow(
    tag: CampaignTagDTO,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val backgroundTint = Color.fromHex(tag.color).copy(alpha = 0.25f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundTint, shape = MaterialTheme.shapes.medium)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                MaterialTheme.shapes.medium,
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = tag.caption,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.White,
            )
            Text(
                text = tag.color,
                color = Color.Gray,
                fontSize = 12.sp,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            AppButton(
                text = "Edit",
                modifier = Modifier.width(80.dp),
                onClick = { onEdit() },
                icon = {
                    Icon(
                        painterResource(Res.drawable.ic_edit_24),
                        contentDescription = null,
                        modifier = Modifier.size(SmallIconSize),
                    )
                },
            )

            AppButton(
                text = "Delete",
                modifier = Modifier.width(80.dp),
                onClick = { onDelete() },
                icon = {
                    Icon(
                        painterResource(Res.drawable.ic_delete_24),
                        contentDescription = null,
                        modifier = Modifier.size(SmallIconSize),
                    )
                },
                color = ColorVariant.Error,
            )
        }
    }
}