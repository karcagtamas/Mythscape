package eu.karcags.mythscape.modules.note.dao

import eu.karcags.mythscape.modules.note.db.NoteCategoriesTable
import eu.karcags.mythscape.modules.application.dao.UserEntity
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class NoteCategoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NoteCategoryEntity>(NoteCategoriesTable)

    var name by NoteCategoriesTable.name
    var creation by NoteCategoriesTable.creation
    var creator by UserEntity referencedOn NoteCategoriesTable.creator
    var lastUpdate by NoteCategoriesTable.lastUpdate
    var lastUpdater by UserEntity referencedOn NoteCategoriesTable.lastUpdater
    var color by NoteCategoriesTable.color
}