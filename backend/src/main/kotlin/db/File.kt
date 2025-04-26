package eu.karcags.mythscape.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Files : IntIdTable("files") {
    val name = varchar("name", 256)
    val bytes = binary("bytes")
    val mimeType = varchar("mime_type", 80)
}

class File(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<File>(Files)

    var name by Files.name
    var bytes by Files.bytes
    var mimeType by Files.mimeType
}