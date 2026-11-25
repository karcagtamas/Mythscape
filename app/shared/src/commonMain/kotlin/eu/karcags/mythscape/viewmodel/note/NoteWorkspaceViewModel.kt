package eu.karcags.mythscape.viewmodel.note

import androidx.lifecycle.ViewModel
import eu.karcags.mythscape.data.note.NoteTab
import eu.karcags.mythscape.data.note.NoteWorkspaceItem
import eu.karcags.mythscape.data.note.NoteWorkspaceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NoteWorkspaceViewModel : ViewModel() {
    private val _state = MutableStateFlow(NoteWorkspaceState())
    val state: StateFlow<NoteWorkspaceState> = _state.asStateFlow()

    fun openNote(note: NoteWorkspaceItem.Note) {
        _state.update { state ->
            val alreadyOpen = state.openTabs.any { it.noteId == note.id }
            val updatedTabs = if (alreadyOpen) {
                state.openTabs
            } else {
                state.openTabs + NoteTab(
                    noteId = note.id,
                    title = note.name,
                    content = note.content,
                )
            }
            state.copy(openTabs = updatedTabs)
        }
    }

    fun selectTab(noteId: Int) {
        _state.update { it.copy(activeNoteId = noteId) }
    }

    fun closeTab(noteId: Int) {
        _state.update {
            val index = it.openTabs.indexOfFirst { t -> t.noteId == noteId }
            if (index == -1) return@update it
            val updated = it.openTabs.filterNot { t -> t.noteId == noteId }
            val nextActiveId = if (it.activeNoteId == noteId) {
                updated.getOrNull((index - 1).coerceAtLeast(0))?.noteId
            } else {
                it.activeNoteId
            }

            it.copy(openTabs = updated, activeNoteId = nextActiveId)
        }
    }

    fun syncTabDraft(noteId: Int, title: String, content: String) {
        _state.update {
            it.copy(
                openTabs = it.openTabs.map { tab ->
                    if (tab.noteId == noteId) {
                        tab.copy(title = title, content = content, isDirty = true)
                    } else {
                        tab
                    }
                }
            )
        }
    }
}