package eu.karcags.mythscape.notedsl

import eu.karcags.mythscape.core.notes.NoteBlock

@DslMarker
annotation class NoteDslMarker

@NoteDslMarker
class NoteDocumentBuilder {
    private val blocks = mutableListOf<NoteBlock>()

    fun section(title: String, init: NoteDocumentBuilder.() -> Unit) {
        val child = NoteDocumentBuilder().apply(init)
        blocks += NoteBlock.Section(title = title, children = child.build())
    }

    fun text(value: String) {
        blocks += NoteBlock.Paragraph(value)
    }

    fun callout(title: String, text: String) {
        blocks += NoteBlock.Callout(title = title, text = text)
    }

    fun list(init: NoteListBuilder.() -> Unit) {
        val listBuilder = NoteListBuilder().apply(init)
        blocks += NoteBlock.BulletList(listBuilder.build())
    }

    fun image(url: String, caption: String? = null) {
        blocks += NoteBlock.Image(url = url, caption = caption)
    }

    fun link(label: String, url: String) {
        blocks += NoteBlock.ExternalLink(label = label, url = url)
    }

    fun noteLink(type: String, id: Int, label: String) {
        blocks += NoteBlock.NoteRef(targetType = type, targetId = id, label = label)
    }

    fun build(): List<NoteBlock> = blocks.toList()
}

@NoteDslMarker
class NoteListBuilder {
    private val items = mutableListOf<String>()

    fun item(text: String) {
        items += text
    }

    fun build(): List<String> = items.toList()
}

fun note(init: NoteDocumentBuilder.() -> Unit): List<NoteBlock> = NoteDocumentBuilder().apply(init).build()
