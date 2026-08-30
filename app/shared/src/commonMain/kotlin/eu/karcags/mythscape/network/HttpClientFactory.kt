package eu.karcags.mythscape.network

import eu.karcags.mythscape.common.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.plugin
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(config: NetworkConfig, sessionManager: SessionManager): HttpClient {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }

            install(DefaultRequest) {
                url(config.baseUrl)
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
        }

        client.plugin(HttpSend).intercept { request ->
            if (!request.url.encodedPath.contains("/api/auth")) {
                sessionManager.getAccessToken()?.let { token ->
                    request.header(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            execute(request)
        }

        return client
    }
}