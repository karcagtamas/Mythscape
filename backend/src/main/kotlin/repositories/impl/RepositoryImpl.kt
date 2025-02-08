package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.repositories.Repository
import eu.karcags.mythscape.utils.suspendTransaction
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

abstract class RepositoryImpl<T : IntEntity> : Repository<T> {
    override suspend fun all(): List<T> = suspendTransaction {
        entityClass().all().toList()
    }

    override suspend fun get(id: Int): T? = suspendTransaction {
        entityClass().findById(id)
    }

    override suspend fun delete(id: Int) = suspendTransaction {
        TODO("Not yet implemented")
    }

    override suspend fun update(id: Int, entity: T) = suspendTransaction {
        TODO("Not yet implemented")
    }

    override suspend fun create(entity: T): Int = suspendTransaction {
        TODO("Not yet implemented")
    }

    abstract fun entityClass(): IntEntityClass<T>
}