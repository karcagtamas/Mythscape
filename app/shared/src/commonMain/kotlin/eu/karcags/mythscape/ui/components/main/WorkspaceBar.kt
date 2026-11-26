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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import eu.karcags.mythscape.core.dtos.campaigns.CampaignDTO
import eu.karcags.mythscape.ui.components.common.avatar.TextAvatar
import eu.karcags.mythscape.data.WorkspaceScreenState
import eu.karcags.mythscape.ui.components.common.avatar.IconAvatar
import mythscape.app.shared.generated.resources.Res
import mythscape.app.shared.generated.resources.ic_add_24
import mythscape.app.shared.generated.resources.ic_calendar_24
import mythscape.app.shared.generated.resources.ic_logout_24
import mythscape.app.shared.generated.resources.ic_person_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkspaceBar(
    state: WorkspaceScreenState,
    campaigns: List<CampaignDTO>,
    onProfileSelect: () -> Unit,
    onCampaignSelect: (CampaignDTO) -> Unit,
    onCampaignCreate: () -> Unit,
    onCalendarSelect: () -> Unit,
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
            IconAvatar(
                icon = Res.drawable.ic_person_24,
                size = 32,
                isSelected = state is WorkspaceScreenState.Profile,
                onClick = { onProfileSelect() }
            )

            IconAvatar(
                icon = Res.drawable.ic_calendar_24,
                size = 32,
                isSelected = state is WorkspaceScreenState.Calendar,
                onClick = { onCalendarSelect() }
            )

            HorizontalDivider(
                modifier = Modifier.width(24.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            )

            campaigns.forEach { campaign ->
                TextAvatar(
                    text = campaign.name,
                    size = 32,
                    isSelected = state is WorkspaceScreenState.CampaignWorkspace && state.campaignId == campaign.id,
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
                Icon(
                    painter = painterResource(Res.drawable.ic_add_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
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
            Icon(
                painter = painterResource(Res.drawable.ic_logout_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}