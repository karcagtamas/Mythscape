package eu.karcags.mythscape.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

val localDateTimeFormat = LocalDateTime.Format {
    year();
    chars("-")
    monthNumber()
    chars("-")
    day()
    chars(" ")
    hour()
    chars(":")
    minute()
    chars(":")
    second()
}

fun LocalDateTime.formatted(): String {
    return format(localDateTimeFormat)
}