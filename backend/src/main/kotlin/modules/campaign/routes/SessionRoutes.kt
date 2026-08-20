package eu.karcags.mythscape.modules.campaign.routes

import eu.karcags.mythscape.dtos.sessions.SessionEditDTO
import eu.karcags.mythscape.dtos.sessions.sessionDTO
import eu.karcags.mythscape.modules.campaign.dao.CampaignEntity
import eu.karcags.mythscape.modules.campaign.dao.SessionEntity
import eu.karcags.mythscape.modules.campaign.db.SessionsTable
import eu.karcags.mythscape.utils.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.core.*

fun Route.sessionRoutes() {
    route("/sessions") {
        get {
            val campaignId = call.queryParameters["campaignId"]?.toIntOrNull()
            val showAll = call.queryParameters["showAll"]?.toBoolean() ?: false

            val sessions = dbQuery {
                SessionEntity.find {
                    val operations = mutableListOf<Op<Boolean>>()

                    if (campaignId != null) {
                        operations.add(SessionsTable.campaign eq campaignId)
                    }

                    if (!showAll) {
                        operations.add(SessionsTable.date greaterEq currentDate())
                    }

                    operations.fold((SessionsTable.id greater 0) as Op<Boolean>) { acc, a ->
                        acc and a
                    }
                }.toList().map { it.sessionDTO() }
            }

            call.wrapped(sessions)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val session = dbQuery {
                SessionEntity.findById(id).required().sessionDTO()
            }

            call.wrapped(session)
        }

        post {
            val dto = call.receive<SessionEditDTO>()

            val session = dbQuery {
                val campaign = CampaignEntity.findById(dto.campaignId).required()

                SessionEntity.new {
                    date = dto.date
                    startTime = dto.startTime
                    endTime = dto.endTime
                    this.campaign = campaign
                }
            }

            call.wrapped(session.id.value, HttpStatusCode.Created)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()
            val dto = call.receive<SessionEditDTO>()

            dbQuery {
                SessionEntity.findByIdAndUpdate(id) {
                    it.date = dto.date
                    it.startTime = dto.startTime
                    it.endTime = dto.endTime
                }.required()
            }

            call.success()
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            dbQuery {
                SessionEntity.findById(id).required().delete()
            }

            call.success()
        }
    }
}