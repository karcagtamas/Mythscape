package eu.karcags

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/mythscape",
        user = "mythscape",
        password = "mythscape",
    )
}