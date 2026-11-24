package eu.karcags.mythscape.ui.components.main.campaign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import eu.karcags.mythscape.dtos.campaigns.CampaignMemberDTO
import eu.karcags.mythscape.ui.components.common.InfoBox
import eu.karcags.mythscape.utils.formatted

@Composable
fun CampaignMemberRow(
    member: CampaignMemberDTO,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    detailsContainer: @Composable ((member: CampaignMemberDTO) -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, MaterialTheme.shapes.medium)
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = member.user?.let { "${it.name} [${it.username}]" } ?: member.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White,
                )
                if (member.isDM) {
                    InfoBox(
                        text = "DM",
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    InfoBox(
                        text = "PLAYER",
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            Text(
                text = "Joined: ${member.creation.formatted()}",
                color = Color.Gray,
                fontSize = 10.sp,
            )
        }

        detailsContainer?.invoke(member)
    }
}