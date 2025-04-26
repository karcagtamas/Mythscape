package eu.karcags.mythscape.controllers

import eu.karcags.mythscape.repositories.FileRepository
import eu.karcags.mythscape.utils.requireNonNull
import eu.karcags.mythscape.utils.required
import io.ktor.http.ContentType
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.*

fun Route.fileController(repository: FileRepository) {
    route("/files") {
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val file = repository.get(id).required()

            call.respondBytes(file.bytes, ContentType.parse(file.mimeType))
        }
    }
}