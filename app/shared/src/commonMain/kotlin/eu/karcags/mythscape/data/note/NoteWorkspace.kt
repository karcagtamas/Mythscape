package eu.karcags.mythscape.data.note

sealed interface NoteWorkspaceItem {
    val id: Int
    val parentId: Int?
    val name: String

    data class Folder(
        override val id: Int,
        override val parentId: Int?,
        override val name: String,
    ) : NoteWorkspaceItem

    data class Note(
        override val id: Int,
        override val parentId: Int?,
        override val name: String,
        val content: String = "",
        val tagId: Int? = null,
    ) : NoteWorkspaceItem
}

data class NoteTab(
    val noteId: Int,
    val title: String,
    val tagId: Int? = null,
    val content: String,
    val isDirty: Boolean = false,
)