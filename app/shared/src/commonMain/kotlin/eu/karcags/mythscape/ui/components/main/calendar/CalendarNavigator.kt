package eu.karcags.mythscape.ui.components.main.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.ui.components.common.AppButton
import mythscape.app.shared.generated.resources.Res
import mythscape.app.shared.generated.resources.ic_chevron_left_24
import mythscape.app.shared.generated.resources.ic_chevron_right_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun CalendarNavigator(
    text: String,
    back: () -> Unit,
    forward: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        AppButton(
            text = "",
            onClick = { back() },
            modifier = Modifier
                .width(32.dp),
            icon = { Icon(painter = painterResource(Res.drawable.ic_chevron_left_24), contentDescription = null) },
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(180.dp),
            textAlign = TextAlign.Center,
        )
        AppButton(
            text = "",
            onClick = { forward() },
            modifier = Modifier
                .width(32.dp),
            icon = { Icon(painter = painterResource(Res.drawable.ic_chevron_right_24), contentDescription = null) }
        )
    }
}