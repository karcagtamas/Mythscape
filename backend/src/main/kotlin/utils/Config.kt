package eu.karcags.mythscape.utils

import com.typesafe.config.ConfigFactory
import eu.karcags.mythscape.ConfigKey
import io.ktor.server.config.*
import io.ktor.server.config.yaml.*

/**
 * Gets string application property.
 * @param key the key of the property
 * @param orElse the return value in case of missing setting
 * @return the found property or the [orElse] value
 */
fun ApplicationConfig.getStringProperty(key: String, orElse: String = ""): String {
    val prop = this.property(key).getString()

    return prop.ifBlank { orElse }
}

/**
 * Gets string application property.
 * @param key the key of the property
 * @param orElse the return value in case of missing setting
 * @return the found property or the [orElse] value
 */
fun ApplicationConfig.getStringProperty(key: ConfigKey, orElse: String = ""): String {
    return this.getStringProperty(key.key, orElse)
}

/**
 * Gets int application property.
 * @param key the key of the property
 * @param orElse the return value in case of missing setting
 * @return the found property or the [orElse] value
 */
fun ApplicationConfig.getIntProperty(key: String, orElse: Int = 0): Int {
    return this.getStringProperty(key, orElse.toString()).toInt()
}

/**
 * Gets int application property.
 * @param key the key of the property
 * @param orElse the return value in case of missing setting
 * @return the found property or the [orElse] value
 */
fun ApplicationConfig.getIntProperty(key: ConfigKey, orElse: Int = 0): Int {
    return this.getIntProperty(key.key, orElse)
}

/**
 * Gets boolean application property.
 * @param key the key of the property
 * @param orElse the return value in case of missing setting
 * @return the found property or the [orElse] value
 */
fun ApplicationConfig.getBooleanProperty(key: String, orElse: Boolean = false): Boolean {
    return this.getStringProperty(key, orElse.toString()).toBoolean()
}

/**
 * Gets boolean application property.
 * @param key the key of the property
 * @param orElse the return value in case of missing setting
 * @return the found property or the [orElse] value
 */
fun ApplicationConfig.getBooleanProperty(key: ConfigKey, orElse: Boolean = false): Boolean {
    return this.getBooleanProperty(key.key, orElse)
}

/**
 * Gets string list application property.
 * @param key the key of the property
 * @return the found property or [emptyList]
 */
fun ApplicationConfig.getStringListProperty(key: String): List<String> {
    return this.property(key).getList()
}

/**
 * Gets string list application property.
 * @param key the key of the property
 * @return the found property or [emptyList]
 */
fun ApplicationConfig.getStringListProperty(key: ConfigKey): List<String> {
    return this.getStringListProperty(key.key)
}

/**
 * Gets the config file by the environment
 * @param env environment name
 * @return the config file name
 */
fun getConfigFile(env: String?, ext: String = ".conf"): String {
    return when (env) {
        "prod" -> "application.prod$ext"
        else -> "application$ext"
    }
}

fun loadYamlConfig(env: String?): ApplicationConfig? {
    return YamlConfig(getConfigFile(env, ".yaml"))
}

fun loadHoconConfig(env: String?): ApplicationConfig {
    return HoconApplicationConfig(ConfigFactory.load(getConfigFile(env)))
}