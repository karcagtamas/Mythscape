package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.modules.application.dao.FileEntity
import eu.karcags.mythscape.repositories.FileRepository
import org.jetbrains.exposed.v1.dao.IntEntityClass

class FileRepositoryImpl : RepositoryImpl<FileEntity>(), FileRepository {

    override fun entityClass(): IntEntityClass<FileEntity> = FileEntity
}