package eu.karcags.mythscape

import eu.karcags.mythscape.core.sayHello

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return sayHello(platform.name)
    }
}