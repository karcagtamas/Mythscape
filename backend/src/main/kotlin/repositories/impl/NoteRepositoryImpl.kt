package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.modules.note.dao.NoteEntity
import eu.karcags.mythscape.repositories.NoteRepository
import org.jetbrains.exposed.v1.dao.IntEntityClass

class NoteRepositoryImpl : RepositoryImpl<NoteEntity>(), NoteRepository {
    override fun entityClass(): IntEntityClass<NoteEntity> = NoteEntity
}