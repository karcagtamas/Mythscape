package eu.karcags.mythscape.ui.main.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.karcags.mythscape.ui.components.common.Page
import eu.karcags.mythscape.ui.components.common.VerticalLine
import eu.karcags.mythscape.viewmodel.note.NoteWorkspaceViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoteWorkspaceScreen(
    campaignId: Int,
    viewModel: NoteWorkspaceViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Page {
        Row(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            if (state.isSidebarVisible) {
                Box(
                    modifier = Modifier
                        .width(260.dp)
                        .fillMaxHeight(),
                )
                VerticalLine()
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                val activeTab = state.activeTab

                if (activeTab != null) {
                    key(activeTab.noteId) {
                        NoteEditorScreen(
                            tab = activeTab,
                            onDraftChanged = { title, content ->
                                viewModel.syncTabDraft(activeTab.noteId, title, content)
                            },
                        )
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        Text(
                            text = "Select or Create a note start writing",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}