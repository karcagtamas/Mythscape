package eu.karcags.mythscape.viewmodel.note

import androidx.lifecycle.ViewModel
import eu.karcags.mythscape.data.note.NoteEditorState
import eu.karcags.mythscape.data.note.NoteTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NoteEditorViewModel : ViewModel() {
    private val _state = MutableStateFlow(NoteEditorState())
    val state: StateFlow<NoteEditorState> = _state.asStateFlow()

    fun loadTab(tab: NoteTab) {
        if (_state.value.noteId == tab.noteId) return

        _state.value = NoteEditorState(
            noteId = tab.noteId,
            title = tab.title,
            content = tab.content,
            isDirty = tab.isDirty,
        )
    }

    fun onTitleChange(newTitle: String) {
        _state.update {
            it.copy(title = newTitle, isDirty = true)
        }
    }

    fun onContentChange(newContent: String) {
        _state.update {
            it.copy(content = newContent, isDirty = true)
        }
    }
}