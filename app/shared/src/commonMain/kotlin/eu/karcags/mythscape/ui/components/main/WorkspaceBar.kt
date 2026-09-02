package eu.karcags.mythscape.ui.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.ui.components.common.TextAvatar
import eu.karcags.mythscape.enums.WorkspaceScreenState

@Composable
fun WorkspaceBar(
    state: WorkspaceScreenState,
    campaigns: List<CampaignDTO>,
    onProfileSelect: () -> Unit,
    onCampaignSelect: (CampaignDTO) -> Unit,
    onCampaignCreate: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(48.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            TextAvatar(
                text = "Profile",
                size = 32,
                isSelected = state is WorkspaceScreenState.Profile,
                onClick = { onProfileSelect() }
            )

            HorizontalDivider(
                modifier = Modifier.width(24.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            )

            campaigns.forEach { campaign ->
                TextAvatar(
                    text = campaign.name,
                    size = 32,
                    isSelected = state is WorkspaceScreenState.CampaignDashboard
                            && state.campaign.id == campaign.id,
                    onClick = { onCampaignSelect(campaign) },
                )
            }

            HorizontalDivider(
                modifier = Modifier.width(24.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
                    .clickable { onCampaignCreate() },
            ) {
                Text(
                    text = "+",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f), CircleShape)
                .clickable {
                    onLogout()
                },
        ) {
            Text(
                text = "➔",
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}