package eu.karcags.mythscape.modules.application.dao

import eu.karcags.mythscape.modules.application.db.FilesTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class FileEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FileEntity>(FilesTable)

    var name by FilesTable.name
    var bytes by FilesTable.bytes
    var mimeType by FilesTable.mimeType
}