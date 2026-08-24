package eu.karcags.mythscape.modules.note.routes

import eu.karcags.mythscape.dtos.notes.noteDataDTO
import eu.karcags.mythscape.modules.note.dao.NoteEntity
import eu.karcags.mythscape.utils.dbQuery
import eu.karcags.mythscape.utils.requireNonNull
import eu.karcags.mythscape.utils.required
import eu.karcags.mythscape.utils.wrapped
import io.ktor.server.routing.*

fun Route.noteRoutes() {
    route("/notes") {
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val note = dbQuery {
                NoteEntity.findById(id).required().noteDataDTO()
            }

            call.wrapped(note)
        }
    }
}