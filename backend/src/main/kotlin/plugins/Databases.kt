package eu.karcags.mythscape.plugins

import eu.karcags.mythscape.ConfigKey
import eu.karcags.mythscape.utils.getStringProperty
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val config = environment.config

    Database.connect(
        config.getStringProperty(ConfigKey.DATABASE_URL),
        user = config.getStringProperty(ConfigKey.DATABASE_USER),
        password = config.getStringProperty(ConfigKey.DATABASE_PASSWORD),
    )
}