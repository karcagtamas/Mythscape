package eu.karcags.mythscape.repositories.impl

import eu.karcags.mythscape.repositories.Repository
import eu.karcags.mythscape.utils.dbQuery
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

abstract class RepositoryImpl<T : IntEntity> : Repository<T> {
    override suspend fun all(): List<T> = dbQuery {
        entityClass().all().toList()
    }

    override suspend fun get(id: Int): T? = dbQuery {
        entityClass().findById(id)
    }

    override suspend fun <U> get(id: Int, mapper: (T) -> U): U? = dbQuery {
        entityClass().findById(id)?.let { mapper(it) }
    }

    override suspend fun delete(id: Int): Unit = dbQuery {
        entityClass().findById(id)?.delete()
    }

    override suspend fun create(fn: T.() -> Unit): Int = dbQuery {
        val result = entityClass().new {
            this.apply(fn)
        }

        result.id.value
    }

    override suspend fun update(id: Int, fn: T.() -> Unit): Unit = dbQuery {
        entityClass().findById(id)?.apply(fn)
    }

    abstract fun entityClass(): IntEntityClass<T>
}