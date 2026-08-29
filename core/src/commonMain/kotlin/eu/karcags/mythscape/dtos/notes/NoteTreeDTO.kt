package eu.karcags.mythscape.dtos.notes

import kotlinx.serialization.Serializable

@Serializable
data class NoteTreeDTO(val key: Key, val name: String, val children: List<NoteTreeDTO>?) {

    @Serializable
    data class Key(val type: Type, val id: Int) {

        enum class Type {
            FOLDER,
            NOTE,
        }
    }
}