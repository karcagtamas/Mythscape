package eu.karcags.mythscape.repositories

import org.jetbrains.exposed.dao.IntEntity

interface Repository<T : IntEntity> {
    suspend fun all(): List<T>
    suspend fun get(id: Int): T?
    suspend fun create(entity: T): Int
    suspend fun update(id: Int, entity: T)
    suspend fun delete(id: Int)
}