package eu.karcags.mythscape

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    Database.connect(
        "jdbc:postgresql://localhost:5433/mythscape",
        user = "mythscape",
        password = "mythscape",
    )
}