package eu.karcags.mythscape.db

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

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