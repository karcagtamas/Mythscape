package eu.karcags.mythscape.core.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvaluateNoteDslRequest(
    val content: String,
)

@Serializable
data class EvaluateNoteDslResponse(
    val blocks: List<NoteBlock>
)

@Serializable
sealed interface NoteBlock {

    @Serializable
    @SerialName("section")
    data class Section(
        val title: String,
        val children: List<NoteBlock>,
    ) : NoteBlock

    @Serializable
    @SerialName("paragraph")
    data class Paragraph(
        val text: String,
    ) : NoteBlock

    @Serializable
    @SerialName("callout")
    data class Callout(
        val title: String,
        val text: String,
    ) : NoteBlock

    @Serializable
    @SerialName("bullet_list")
    data class BulletList(
        val items: List<String>
    ) : NoteBlock

    @Serializable
    @SerialName("image")
    data class Image(
        val url: String,
        val caption: String? = null,
    ) : NoteBlock

    @Serializable
    @SerialName("external_link")
    data class ExternalLink(
        val label: String,
        val url: String,
    ) : NoteBlock

    @Serializable
    @SerialName("note_ref")
    data class NoteRef(
        val targetType: String,
        val targetId: Int,
        val label: String,
    ) : NoteBlock

    @Serializable
    @SerialName("error")
    data class SyntaxError(
        val lineNumber: Int? = null,
        val message: String,
    ) : NoteBlock

}