package eu.karcags.mythscape.data.note

data class NoteWorkspaceState(
    val items: List<NoteWorkspaceItem> = emptyList(),
    val openTabs: List<NoteTab> = listOf(
        NoteTab(noteId = -1, title = "Untitled Note", content = ""),
    ),
    val activeNoteId: Int? = -1,
    val isSidebarVisible: Boolean = false,
) {
    val activeTab: NoteTab?
        get() = openTabs.find { it.noteId == activeNoteId }
}