package eu.karcags.mythscape

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform