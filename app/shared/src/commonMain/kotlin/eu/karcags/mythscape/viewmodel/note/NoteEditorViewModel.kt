package eu.karcags.mythscape.viewmodel.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.core.notes.NoteBlock
import eu.karcags.mythscape.data.note.NoteEditorState
import eu.karcags.mythscape.data.note.NoteTab
import eu.karcags.mythscape.enums.EditorViewMode
import eu.karcags.mythscape.network.NoteDslRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
class NoteEditorViewModel(
    private val noteDslRepository: NoteDslRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(NoteEditorState())
    val state: StateFlow<NoteEditorState> = _state.asStateFlow()

    private val contentChanges = MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        viewModelScope.launch {
            contentChanges
                .debounce(500L.milliseconds)
                .distinctUntilChanged()
                .collectLatest { rawDsl ->
                    evaluateOnBackend(rawDsl)
                }
        }
    }

    fun loadTab(tab: NoteTab) {
        if (_state.value.noteId == tab.noteId) return

        _state.value = NoteEditorState(
            noteId = tab.noteId,
            title = tab.title,
            tagId = tab.tagId,
            content = tab.content,
            isDirty = tab.isDirty,
        )
        if (tab.content.isNotBlank()) {
            viewModelScope.launch { evaluateOnBackend(tab.content) }
        }
    }

    fun onTitleChange(newTitle: String) {
        _state.update {
            it.copy(title = newTitle, isDirty = true)
        }
    }

    fun onTagChange(newTagId: Int?) {
        _state.update {
            it.copy(tagId = newTagId, isDirty = true)
        }
    }

    fun onContentChange(newContent: String) {
        _state.update {
            it.copy(content = newContent, isDirty = true)
        }
        contentChanges.tryEmit(newContent)
    }

    fun setViewMode(mode: EditorViewMode) {
        _state.update { it.copy(viewMode = mode) }
        if (mode != EditorViewMode.Code && _state.value.content.isNotBlank()) {
            viewModelScope.launch { evaluateOnBackend(_state.value.content) }
        }
    }

    fun appendSnippet(snippet: String): String {
        val current = _state.value.content
        val updated = if (current.isBlank()) snippet else "$current\n\n$snippet"
        onContentChange(updated)
        return updated
    }

    private suspend fun evaluateOnBackend(rawDsl: String) {
        if (rawDsl.isBlank()) {
            _state.update { it.copy(blocks = emptyList(), isEvaluating = false) }
            return
        }

        _state.update { it.copy(isEvaluating = true) }
        try {
            val evaluatedBlocks = noteDslRepository.evaluate(rawDsl)
            _state.update { it.copy(blocks = evaluatedBlocks, isEvaluating = false) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    blocks = listOf(NoteBlock.SyntaxError(message = e.message ?: "Failed to evaluate DSL")),
                    isEvaluating = false,
                )
            }
        }
    }
}