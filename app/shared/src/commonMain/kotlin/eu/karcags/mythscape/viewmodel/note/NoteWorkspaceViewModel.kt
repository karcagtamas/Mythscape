package eu.karcags.mythscape.viewmodel.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.data.note.NoteTab
import eu.karcags.mythscape.data.note.NoteWorkspaceItem
import eu.karcags.mythscape.data.note.NoteWorkspaceState
import eu.karcags.mythscape.core.dtos.campaigns.CampaignTagDTO
import eu.karcags.mythscape.network.CampaignRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class NoteWorkspaceViewModel(
    private val campaignRepository: CampaignRepository,
) : ViewModel() {
    private val _campaignId = MutableStateFlow<Int?>(null)

    private val _state = MutableStateFlow(NoteWorkspaceState())
    val state: StateFlow<NoteWorkspaceState> = _state.asStateFlow()

    val tags: StateFlow<List<CampaignTagDTO>> = _campaignId
        .filterNotNull()
        .distinctUntilChanged()
        .map { id ->
            try {
                val res = campaignRepository.getCampaignTags(id)
                if (res.success && res.data != null) {
                    res.data.orEmpty()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    currentCoroutineContext().ensureActive()
                }
                emptyList()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    fun setCampaignId(id: Int) {
        _campaignId.value = id
    }

    fun openNote(note: NoteWorkspaceItem.Note) {
        _state.update { state ->
            val alreadyOpen = state.openTabs.any { it.noteId == note.id }
            val updatedTabs = if (alreadyOpen) {
                state.openTabs
            } else {
                state.openTabs + NoteTab(
                    noteId = note.id,
                    title = note.name,
                    tagId = note.tagId,
                    content = note.content,
                )
            }
            state.copy(openTabs = updatedTabs, activeNoteId = note.id)
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

    fun createNewDraftTab() {
        _state.update {
            val minId = it.openTabs.minOfOrNull { t -> t.noteId } ?: 0
            val nextTempId = if (minId < 0) minId - 1 else -1
            val newTab = NoteTab(
                noteId = nextTempId,
                title = "Untitled Note",
                tagId = null,
                content = "",
                isDirty = false,
            )
            it.copy(
                openTabs = it.openTabs + newTab,
                activeNoteId = nextTempId,
            )
        }
    }

    fun syncTabDraft(noteId: Int, title: String, tagId: Int?, content: String) {
        _state.update {
            it.copy(
                openTabs = it.openTabs.map { tab ->
                    if (tab.noteId == noteId) {
                        tab.copy(title = title, tagId = tagId, content = content, isDirty = true)
                    } else {
                        tab
                    }
                }
            )
        }
    }
}