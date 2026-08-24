package eu.karcags.mythscape.utils

import io.ktor.http.*

fun <T> T?.required(): T {
    if (this == null) {
        throw ServerException.NotFound()
    }

    return this
}

fun <T> T?.requireNonNull(): T {
    if (this == null) {
        throw ServerException("The checked value is null", HttpStatusCode.BadRequest)
    }

    return this
}