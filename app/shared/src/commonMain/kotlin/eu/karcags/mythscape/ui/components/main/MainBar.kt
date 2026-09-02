package eu.karcags.mythscape.ui.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.enums.WorkspaceScreenState

@Composable
fun MainBar(
    state: WorkspaceScreenState,
    onAppTitleClick: () -> Unit,
    onVersionClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(26.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "MythScape",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .clickable { onAppTitleClick() }
            )
            Text(" | ", color = Color.DarkGray, fontSize = 10.sp)
            Text(
                text = when (state) {
                    is WorkspaceScreenState.Dashboard -> "General Feed"
                    is WorkspaceScreenState.Profile -> "Profile"
                    is WorkspaceScreenState.CampaignDashboard -> "Campaign // ${state.campaign.title}"
                    is WorkspaceScreenState.CampaignCreate -> "Campaign create"
                    is WorkspaceScreenState.CampaignEdit -> "Campaign edit // ${state.campaign.title}"
                },
                color = Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        Text(
            "V1.0.0",
            color = Color.DarkGray,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable {
                    onVersionClick()
                }
        )
    }
}