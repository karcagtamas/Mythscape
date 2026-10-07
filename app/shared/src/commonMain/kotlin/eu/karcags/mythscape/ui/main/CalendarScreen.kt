package eu.karcags.mythscape.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.karcags.mythscape.ui.components.common.LoadingBox
import eu.karcags.mythscape.ui.components.common.Page
import eu.karcags.mythscape.ui.components.common.PageHeader
import eu.karcags.mythscape.viewmodel.CalendarViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = koinViewModel(),
) {
    val sessions by viewModel.sessions.collectAsState()
    val today = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }

    Page {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PageHeader(
                "Calendar",
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                LoadingBox(
                    isLoading = viewModel.isLoading,
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {

                    }
                }
            }
        }
    }
}