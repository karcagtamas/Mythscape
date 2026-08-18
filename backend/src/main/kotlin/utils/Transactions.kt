package eu.karcags.mythscape.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> suspendTransaction(block: suspend () -> T): T = withContext(Dispatchers.IO) {
    block()
}