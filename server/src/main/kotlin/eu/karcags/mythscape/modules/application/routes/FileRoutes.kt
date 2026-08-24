package eu.karcags.mythscape.modules.application.routes

import eu.karcags.mythscape.modules.application.dao.FileEntity
import eu.karcags.mythscape.utils.dbQuery
import eu.karcags.mythscape.utils.requireNonNull
import eu.karcags.mythscape.utils.required
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.fileRoutes() {
    route("/files") {
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val file = dbQuery {
                FileEntity.findById(id).required()
            }

            call.respondBytes(file.bytes, ContentType.parse(file.mimeType))
        }
    }
}