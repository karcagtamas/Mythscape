package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.db.File
import eu.karcags.mythscape.repositories.FileRepository
import org.jetbrains.exposed.v1.dao.IntEntityClass

class FileRepositoryImpl : RepositoryImpl<File>(), FileRepository {

    override fun entityClass(): IntEntityClass<File> = File
}