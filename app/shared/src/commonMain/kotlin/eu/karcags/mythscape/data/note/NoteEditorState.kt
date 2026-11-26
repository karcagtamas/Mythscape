package eu.karcags.mythscape.data.note

import eu.karcags.mythscape.core.notes.NoteBlock
import eu.karcags.mythscape.enums.EditorViewMode

data class NoteEditorState(
    val noteId: Int = -1,
    val title: String = "",
    val tagId: Int? = null,
    val content: String = "",
    val blocks: List<NoteBlock> = emptyList(),
    val isEvaluating: Boolean = false,
    val viewMode: EditorViewMode = EditorViewMode.Split,
    val isDirty: Boolean = false,
)