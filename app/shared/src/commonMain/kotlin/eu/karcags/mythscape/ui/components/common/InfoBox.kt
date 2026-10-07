package eu.karcags.mythscape.ui.components.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoBox(
    color: Color,
) {
    Text(
        "Canceled",
        color = color,
        fontSize = 10.sp,
        fontWeight = FontWeight.Black,
        modifier = Modifier
            .border(
                1.dp,
                color,
                MaterialTheme.shapes.medium,
            )
            .padding(horizontal = 4.dp, vertical = 1.dp),
    )
}