package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.db.Note
import eu.karcags.mythscape.repositories.NoteRepository
import org.jetbrains.exposed.dao.IntEntityClass

class NoteRepositoryImpl : RepositoryImpl<Note>(), NoteRepository {
    override fun entityClass(): IntEntityClass<Note> = Note
}