package eu.karcags.mythscape.utils

import io.ktor.http.*

open class ServerException(message: String, val statusCode: HttpStatusCode) : Exception(message) {

    class NotFound : ServerException("Resource not found", HttpStatusCode.NotFound)

    class Unauthorized(message: String) : ServerException(message, HttpStatusCode.Unauthorized)

    class Forbidden(message: String) : ServerException(message, HttpStatusCode.Forbidden)
}