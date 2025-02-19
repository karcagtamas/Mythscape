package eu.karcags.mythscape.utils

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
class Success<out T : Any>(val data: T?, val statusCode: Int = HttpStatusCode.OK.value) {
    val success: Boolean = true
    val error: Unit? = null
}

@Serializable
class ErrorData(val message: String?, val stackTrace: List<String> = emptyList())

@Serializable
class Failure(val statusCode: Int = HttpStatusCode.InternalServerError.value, val error: ErrorData? = null) {
    val data: Unit? = null
    val success: Boolean = false
}

fun <T : Any> T?.wrap(statusCode: HttpStatusCode = HttpStatusCode.OK): Success<T> {
    return Success(this, statusCode.value)
}

fun failure(statusCode: HttpStatusCode = HttpStatusCode.InternalServerError): Failure {
    return Failure(statusCode.value)
}

fun <T : Throwable> T.failure(statusCode: HttpStatusCode = HttpStatusCode.InternalServerError): Failure {
    return Failure(statusCode.value, error = ErrorData(message, stackTrace.map { it.toString() }))
}

fun ServerException.failure(): Failure {
    return Failure(statusCode.value, ErrorData(message, stackTrace.map { it.toString() }))
}