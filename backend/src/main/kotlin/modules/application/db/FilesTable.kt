package eu.karcags.mythscape.modules.application.db

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object FilesTable : IntIdTable("files") {
    val name = varchar("name", 256)
    val bytes = binary("bytes")
    val mimeType = varchar("mime_type", 80)
}