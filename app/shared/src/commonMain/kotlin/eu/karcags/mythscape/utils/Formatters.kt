package eu.karcags.mythscape.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
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

fun DayOfWeek.short(): String {
    return name.uppercase().take(3)
}

fun Month.short(): String {
    return name.uppercase().take(3)
}