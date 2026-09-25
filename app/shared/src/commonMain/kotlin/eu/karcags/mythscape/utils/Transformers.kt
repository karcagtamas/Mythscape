package eu.karcags.mythscape.utils

fun String?.nullIfEmpty(): String? {
    return if (this.isNullOrBlank()) null else this
}