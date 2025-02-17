package eu.karcags.mythscape

import eu.karcags.mythscape.plugins.configureAuthentication
import eu.karcags.mythscape.plugins.configureDatabases
import eu.karcags.mythscape.plugins.configureKoin
import eu.karcags.mythscape.plugins.configureRouting
import eu.karcags.mythscape.utils.getBooleanProperty
import eu.karcags.mythscape.utils.getIntProperty
import eu.karcags.mythscape.utils.getStringProperty
import eu.karcags.mythscape.utils.loadYamlConfig
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.event.Level
import java.io.File
import java.security.KeyStore

fun main(args: Array<String>) {
    embeddedServer(Netty, environment = applicationEnvironment {
        config = loadYamlConfig(args.getOrNull(0))!!
    }, configure = {
        val config = loadYamlConfig(args.getOrNull(0))!!
        connector {
            host = config.getStringProperty(ConfigKey.SERVER_HOST, "localhost")
            port = config.getIntProperty(ConfigKey.SERVER_PORT, 8080)
        }

        if (config.getBooleanProperty(ConfigKey.SSL_USE, false)) {
            val keyStoreFile = File(config.getStringProperty(ConfigKey.SSL_KEYSTORE, "keystore.jks"))
            val key = config.getStringProperty(ConfigKey.SSL_KEYSTORE_PASSWORD, "key")

            sslConnector(
                keyStore = KeyStore.getInstance("JKS").apply { load(keyStoreFile.inputStream(), key.toCharArray()) },
                keyAlias = config.getStringProperty(ConfigKey.SSL_KEY_ALIAS, "alias"),
                keyStorePassword = { key.toCharArray() },
                privateKeyPassword = { key.toCharArray() },
            ) {
                keyStorePath = keyStoreFile
                port = config.getIntProperty(ConfigKey.SSL_PORT, 8081)
            }
        }
    }, module = {
        mainModule()
    }).start(wait = true)
}

fun Application.mainModule() {
    configureKoin()
    configureAuthentication()
    configureDatabases()

    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    configureRouting()

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}


