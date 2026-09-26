package eu.karcags.mythscape.ui.main.campaign

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.enums.CampaignWorkspaceScreenState
import eu.karcags.mythscape.ui.components.common.HorizontalLine
import eu.karcags.mythscape.viewmodel.campaign.CampaignWorkspaceViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CampaignWorkspaceScreen(
    campaignId: Int,
    onEdit: (CampaignDTO) -> Unit,
    onDelete: () -> Unit,
    onArchive: () -> Unit,
    viewModel: CampaignWorkspaceViewModel = koinViewModel(),
) {
    LaunchedEffect(campaignId) {
        viewModel.initialize(campaignId)
        viewModel.resetToDashBoard()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            TabMenuButton(
                text = "Dashboard",
                isSelected = viewModel.screenState is CampaignWorkspaceScreenState.Dashboard,
                onClick = { viewModel.selectTab(CampaignWorkspaceScreenState.Dashboard) }
            )

            TabMenuButton(
                text = "Sessions",
                isSelected = viewModel.screenState is CampaignWorkspaceScreenState.Sessions,
                onClick = { viewModel.selectTab(CampaignWorkspaceScreenState.Sessions) }
            )

            TabMenuButton(
                text = "Notes",
                isSelected = viewModel.screenState is CampaignWorkspaceScreenState.Notes,
                onClick = { viewModel.selectTab(CampaignWorkspaceScreenState.Notes) }
            )
        }

        HorizontalLine(alpha = 0.5f)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            viewModel.campaign?.let { campaign ->
                when (viewModel.screenState) {
                    is CampaignWorkspaceScreenState.Dashboard -> {
                        CampaignDashboardScreen(
                            campaign = campaign,
                            onEdit = {
                                viewModel.initialize(campaign.id)
                                onEdit(it)
                            },
                            onDelete = { onDelete() },
                            onArchive = { onArchive() },
                        )
                    }

                    is CampaignWorkspaceScreenState.Sessions -> {
                        CampaignSessionsScreen(campaignId = campaign.id)
                    }

                    is CampaignWorkspaceScreenState.Notes -> {
                        Box(
                            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "NOTEBOOK TREE SYSTEM & SCRIPTABLE MONACO CODE EDITOR CANVAS",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabMenuButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent
    val textColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = 2.dp)
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
    ) {
        Text(
            text = text.uppercase(),
            color = textColor,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            letterSpacing = 0.5.sp,
        )
    }
}