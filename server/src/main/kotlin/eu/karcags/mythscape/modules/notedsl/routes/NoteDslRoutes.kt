package eu.karcags.mythscape.modules.notedsl.routes

import eu.karcags.mythscape.core.notes.EvaluateNoteDslRequest
import eu.karcags.mythscape.core.notes.EvaluateNoteDslResponse
import eu.karcags.mythscape.notedsl.NoteDslEvaluator
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.noteDslRoutes() {
    val evaluator by inject<NoteDslEvaluator>()

    post("/notes/evaluate-dsl") {
        val req = call.receive<EvaluateNoteDslRequest>()
        val blocks = evaluator.evaluate(req.content)
        call.respond(HttpStatusCode.OK, EvaluateNoteDslResponse(blocks = blocks))
    }
}