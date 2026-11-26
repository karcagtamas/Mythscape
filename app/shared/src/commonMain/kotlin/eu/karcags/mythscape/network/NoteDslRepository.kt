package eu.karcags.mythscape.network

import eu.karcags.mythscape.core.notes.EvaluateNoteDslRequest
import eu.karcags.mythscape.core.notes.EvaluateNoteDslResponse
import eu.karcags.mythscape.core.notes.NoteBlock
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class NoteDslRepository(
    private val client: HttpClient,
) {

    suspend fun evaluate(content: String): List<NoteBlock> {
        val response = client.post("/api/notes/evaluate-dsl") {
            setBody(EvaluateNoteDslRequest(content = content))
        }
        return (Json.decodeFromString<EvaluateNoteDslResponse>(response.bodyAsText())).blocks
    }
}