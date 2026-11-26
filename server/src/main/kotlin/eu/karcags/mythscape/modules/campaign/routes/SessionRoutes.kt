package eu.karcags.mythscape.modules.campaign.routes

import eu.karcags.mythscape.core.dtos.sessions.SessionRequestDTO
import eu.karcags.mythscape.modules.campaign.dao.CampaignEntity
import eu.karcags.mythscape.modules.campaign.dao.SessionEntity
import eu.karcags.mythscape.modules.campaign.db.SessionsTable
import eu.karcags.mythscape.utils.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.jetbrains.exposed.v1.core.*

fun Route.sessionRoutes() {
    route("/sessions") {
        get {
            val campaignId = call.queryParameters["campaignId"]?.toIntOrNull()
            val showCanceled = call.queryParameters["showCanceled"]?.toBoolean() ?: false
            val page = call.queryParameters["page"]?.toIntOrNull() ?: 0
            val size = call.queryParameters["size"]?.toIntOrNull() ?: 15
            val after = call.queryParameters["after"]?.let { LocalDate.parse(it) }
            val before = call.queryParameters["before"]?.let { LocalDate.parse(it) }

            val sessions = dbQuery {
                SessionEntity
                    .find {
                        queryWhere {
                            if (campaignId != null) {
                                add(SessionsTable.campaign eq campaignId)
                            }

                            if (!showCanceled) {
                                add(SessionsTable.canceled eq Op.FALSE)
                            }

                            after?.also {
                                add(SessionsTable.date greaterEq it)
                            }

                            before?.also {
                                add(SessionsTable.date lessEq it)
                            }
                        }
                    }
                    .orderBy(
                        SessionsTable.date to SortOrder.DESC,
                        SessionsTable.startTime to SortOrder.DESC,
                    )
                    .limit(size)
                    .offset((page * size).toLong())
                    .map { it.dto() }
            }

            call.wrapped(sessions)
        }

        get("/agenda") {
            val date = call.queryParameters["date"].requireNonNull().let { LocalDate.parse(it) }

            val sessions = dbQuery {
                SessionEntity
                    .find {
                        (SessionsTable.date greaterEq date) and (SessionsTable.date lessEq date.plus(
                            1,
                            DateTimeUnit.MONTH
                        ))
                    }
                    .map { it.dto() }
            }

            call.wrapped(sessions)
        }

        get("/recent") {
            val campaignId = call.queryParameters["campaignId"]?.toIntOrNull()
            val number = call.queryParameters["number"]?.toIntOrNull() ?: 3

            val sessions = dbQuery {
                SessionEntity
                    .find {
                        queryWhere {
                            if (campaignId != null) {
                                add(SessionsTable.campaign eq campaignId)
                            }

                            add(SessionsTable.date lessEq currentDate())
                        }
                    }
                    .orderBy(
                        SessionsTable.date to SortOrder.DESC,
                        SessionsTable.startTime to SortOrder.ASC,
                    )
                    .take(number)
                    .toList().map { it.dto() }
            }

            call.wrapped(sessions)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()

            val session = dbQuery {
                SessionEntity.findById(id).required().dto()
            }

            call.wrapped(session)
        }

        post {
            val dto = call.receive<SessionRequestDTO>()

            val data = dbQuery {
                val campaign = CampaignEntity.findById(dto.campaignId).required()

                SessionEntity.new {
                    date = dto.date
                    startTime = dto.startTime
                    endTime = dto.endTime
                    this.campaign = campaign
                }.dto()
            }

            call.wrapped(data, HttpStatusCode.Created)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull().requireNonNull()
            val dto = call.receive<SessionRequestDTO>()

            val data = dbQuery {
                SessionEntity.findByIdAndUpdate(id) {
                    it.date = dto.date
                    it.startTime = dto.startTime
                    it.endTime = dto.endTime
                    it.canceled = dto.canceled
                }.required().dto()
            }

            call.wrapped(data)
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