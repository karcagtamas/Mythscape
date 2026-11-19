package eu.karcags.mythscape.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.format
import kotlinx.datetime.minus
import kotlinx.datetime.plus

val localDateTimeFormat = LocalDateTime.Format {
    year()
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

val localDateFormat = LocalDate.Format {
    year()
    chars("-")
    monthNumber()
    chars("-")
    day()
}

fun LocalDateTime.formatted(): String {
    return format(localDateTimeFormat)
}

fun LocalDate.formatted(): String {
    return format(localDateFormat)
}

fun LocalDate.humanify(today: LocalDate? = null): String {
    if (today == null) return formatted()

    if (this == today) return "Today"

    if (this == today.plus(1, DateTimeUnit.DAY)) return "Tomorrow"

    if (this == today.minus(1, DateTimeUnit.DAY)) return "Yesterday"

    return formatted()
}

fun DayOfWeek.short(): String {
    return name.uppercase().take(3)
}

fun Month.short(): String {
    return name.uppercase().take(3)
}

fun Long.formatted(suffix: String? = null): String {
    if (suffix == null) return this.toString()

    return "$this $suffix"
}

fun Int.formatted(suffix: String? = null): String {
    return this.toLong().formatted(suffix)
}

fun Int.padded(length: Int = 2, character: Char = '0'): String {
    return toString().padStart(length, character)
}