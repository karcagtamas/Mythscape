package eu.karcags.mythscape.data.note

data class NoteEditorState(
    val noteId: Int = -1,
    val title: String = "",
    val content: String = "",
    val isDirty: Boolean = false,
)