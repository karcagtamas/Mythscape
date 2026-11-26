package eu.karcags.mythscape.ui.main.note

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.karcags.mythscape.data.note.NoteTab
import eu.karcags.mythscape.ui.components.common.HorizontalLine
import eu.karcags.mythscape.ui.components.common.Page
import eu.karcags.mythscape.ui.components.common.VerticalLine
import eu.karcags.mythscape.viewmodel.note.NoteWorkspaceViewModel
import mythscape.app.shared.generated.resources.Res
import mythscape.app.shared.generated.resources.ic_add_24
import mythscape.app.shared.generated.resources.ic_close_24
import org.jetbrains.compose.resources.painterResource
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
                TabBar(
                    tabs = state.openTabs,
                    activeNoteId = state.activeNoteId,
                    onSelectTab = viewModel::selectTab,
                    onCloseTab = viewModel::closeTab,
                    onNewTab = viewModel::createNewDraftTab,
                )
                HorizontalLine()

                val activeTab = state.activeTab

                if (activeTab != null) {
                    key(activeTab.noteId) {
                        NoteEditorScreen(
                            tab = activeTab,
                            onDraftChanged = { title, content, tagId ->
                                viewModel.syncTabDraft(activeTab.noteId, title, tagId, content)
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
                            text = "No note open. Click + above to create a note.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TabBar(
    tabs: List<NoteTab>,
    activeNoteId: Int?,
    onSelectTab: (Int) -> Unit,
    onCloseTab: (Int) -> Unit,
    onNewTab: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 6.dp),
        ) {
            tabs.forEach { tab ->
                val isSelected = tab.noteId == activeNoteId
                TabItem(
                    tab = tab,
                    isSelected = isSelected,
                    onSelect = { onSelectTab(tab.noteId) },
                    onClose = { onCloseTab(tab.noteId) },
                    modifier = Modifier.padding(end = 6.dp),
                )
            }

            IconButton(
                onClick = onNewTab,
                modifier = Modifier
                    .size(32.dp)
                    .pointerHoverIcon(PointerIcon.Hand),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add_24),
                    contentDescription = "New Note tab",
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Composable
private fun TabItem(
    tab: NoteTab,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (isSelected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.outlineVariant
            },
        ),
        modifier = modifier
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onSelect),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 12.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
        ) {
            val displayTitle = buildString {
                append(tab.title.ifBlank { "Untitled" })
                if (tab.isDirty) append(" *")
            }

            Text(
                text = displayTitle,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .widthIn(max = 180.dp),
            )

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(20.dp)
                    .pointerHoverIcon(PointerIcon.Hand),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_close_24),
                    contentDescription = "Close ${tab.title}",
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}