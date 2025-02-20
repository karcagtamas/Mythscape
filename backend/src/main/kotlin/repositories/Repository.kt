package eu.karcags.mythscape.repositories

import org.jetbrains.exposed.dao.IntEntity

interface Repository<T : IntEntity> {
    suspend fun all(): List<T>
    suspend fun get(id: Int): T?
    suspend fun <U> get(id: Int, mapper: (T) -> U): U?
    suspend fun create(fn: T.() -> Unit): Int
    suspend fun update(id: Int, fn: T.() -> Unit)
    suspend fun delete(id: Int)
}