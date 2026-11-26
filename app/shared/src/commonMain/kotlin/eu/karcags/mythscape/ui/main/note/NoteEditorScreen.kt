package eu.karcags.mythscape.ui.main.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.karcags.mythscape.data.note.NoteTab
import eu.karcags.mythscape.ui.components.common.HorizontalLine
import eu.karcags.mythscape.viewmodel.note.NoteEditorViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoteEditorScreen(
    tab: NoteTab,
    onDraftChanged: (title: String, content: String, tagId: Int?) -> Unit,
    viewModel: NoteEditorViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(tab.noteId) {
        viewModel.loadTab(tab)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BasicTextField(
            value = state.title,
            onValueChange = { newTitle ->
                viewModel.onTitleChange(newTitle)
                onDraftChanged(newTitle, state.content, null)
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                if (state.title.isEmpty()) {
                    Text(
                        text = "Note title...",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                innerTextField()
            },
            modifier = Modifier
                .fillMaxWidth(),
        )

        HorizontalLine()

        BasicTextField(
            value = state.content,
            onValueChange = { newContent ->
                viewModel.onContentChange(newContent)
                onDraftChanged(state.title, newContent, null)
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                if (state.content.isEmpty()) {
                    Text(
                        text = "Start writing...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                innerTextField()
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 300.dp),
        )
    }
}