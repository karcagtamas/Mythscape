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

    override suspend fun <U> get(id: Int, mapper: (T) -> U): U? = suspendTransaction {
        entityClass().findById(id)?.let { mapper(it) }
    }

    override suspend fun delete(id: Int): Unit = suspendTransaction {
        entityClass().findById(id)?.delete()
    }

    override suspend fun create(fn: T.() -> Unit): Int = suspendTransaction {
        val result = entityClass().new {
            this.apply(fn)
        }

        result.id.value
    }

    override suspend fun update(id: Int, fn: T.() -> Unit): Unit = suspendTransaction {
        entityClass().findById(id)?.apply(fn)
    }

    abstract fun entityClass(): IntEntityClass<T>
}