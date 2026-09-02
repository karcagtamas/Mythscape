package eu.karcags.mythscape.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import eu.karcags.mythscape.ui.components.common.LoadingBox
import eu.karcags.mythscape.ui.components.common.MetaRow
import eu.karcags.mythscape.ui.components.common.PrimaryCard
import eu.karcags.mythscape.ui.components.common.SecondaryCard
import eu.karcags.mythscape.utils.formatted
import eu.karcags.mythscape.viewmodel.CampaignDashboardViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CampaignDashboardScreen(
    campaignId: Int,
    onEdit: (CampaignDTO) -> Unit,
    viewModel: CampaignDashboardViewModel = koinViewModel(),
) {
    LaunchedEffect(campaignId) {
        viewModel.initialize(campaignId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(18.dp),
        contentAlignment = Alignment.TopStart,
    ) {
        LoadingBox(
            isLoading = viewModel.isLoading,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    viewModel.campaign?.let { campaign ->
                        PrimaryCard(
                            title = campaign.title,
                        ) {
                            Text(
                                text = campaign.description ?: "No description provided",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            MetaRow(label = "Created By", value = campaign.creator.name)
                            MetaRow(label = "Launch", value = campaign.creation.formatted())
                            MetaRow(label = "Last Update", value = campaign.lastUpdate.formatted())
                        }
                    }

                    SecondaryCard(
                        title = "Recent Sessions",
                        modifier = Modifier
                            .weight(1f),
                    ) {
                        if (viewModel.recentSessions.isEmpty()) {
                            Text(
                                "No recent sessions available",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp
                            )
                        } else {
                            viewModel.recentSessions.forEach { session ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "Session ${session.id}",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Text(
                                        text = "${session.date} [${session.startTime.hour}:${
                                            session.startTime.minute.toString().padStart(2, '0')
                                        } - ${session.endTime.hour}:${
                                            session.endTime.minute.toString().padStart(2, '0')
                                        }]",
                                        color = Color.Gray,
                                        fontSize = 11.sp,
                                    )
                                }
                            }
                        }
                    }
                }

                SecondaryCard(
                    title = "Members",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(viewModel.members) { member ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .padding(vertical = 6.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column {
                                    Text(
                                        text = member.name,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Text(
                                        text = "Joined: ${member.creation.formatted()}",
                                        color = Color.DarkGray,
                                        fontSize = 10.sp,
                                    )
                                }

                                if (member.isDM) {
                                    Text(
                                        text = "DM",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
                                            .padding(horizontal = 4.dp, vertical = 2.dp),
                                    )
                                } else {
                                    Text(
                                        text = "PLAYER",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}