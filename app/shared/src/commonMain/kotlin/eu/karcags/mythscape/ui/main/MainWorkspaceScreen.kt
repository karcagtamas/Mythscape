package eu.karcags.mythscape.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.ui.components.TextAvatar
import eu.karcags.mythscape.ui.dashboard.DashboardScreen
import eu.karcags.mythscape.viewmodel.AppViewModel
import eu.karcags.mythscape.viewmodel.WorkspaceViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainWorkspaceScreen(
    appViewModel: AppViewModel = koinViewModel(),
    workspaceViewModel: WorkspaceViewModel = koinViewModel(),
) {
    val campaigns by workspaceViewModel.campaigns.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
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
                    isSelected = workspaceViewModel.activeView == ScreenFocus.PROFILE,
                    onClick = { workspaceViewModel.selectProfile() }
                )

                HorizontalDivider(
                    modifier = Modifier.width(24.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                )

                campaigns.forEach { campaign ->
                    TextAvatar(
                        text = campaign.name,
                        size = 32,
                        isSelected = workspaceViewModel.activeView == ScreenFocus.CAMPAIGN_DASHBOARD
                                && workspaceViewModel.selectedCampaign?.id == campaign.id,
                        onClick = { workspaceViewModel.selectCampaign(campaign) },
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
                        .clickable {},
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
                        appViewModel.logout()
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

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(MaterialTheme.colorScheme.outline),
        ) {}

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
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
                            .clickable {
                                workspaceViewModel.selectDashboard()
                            }
                    )
                    Text(" | ", color = Color.DarkGray, fontSize = 10.sp)
                    Text(
                        text = when (workspaceViewModel.activeView) {
                            ScreenFocus.DASHBOARD -> "General Feed"
                            ScreenFocus.PROFILE -> "Profile"
                            ScreenFocus.CAMPAIGN_DASHBOARD -> "Campaign // ${workspaceViewModel.selectedCampaign?.title}"
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
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outline),
            ) {}

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                when (workspaceViewModel.activeView) {
                    ScreenFocus.DASHBOARD -> {
                        DashboardScreen()
                    }

                    ScreenFocus.PROFILE -> {
                        ProfileScreen()
                    }

                    ScreenFocus.CAMPAIGN_DASHBOARD -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "CAMPAIGN MANAGER SCREEN FOR: ${workspaceViewModel.selectedCampaign?.name}",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}