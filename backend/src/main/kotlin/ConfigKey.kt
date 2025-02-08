package eu.karcags.mythscape

enum class ConfigKey(val key: String) {
    DATABASE_USER("database.user"),
    DATABASE_PASSWORD("database.password"),
    DATABASE_URL("database.url"),
    SERVER_HOST("server.host"),
    SERVER_PORT("server.port"),
    SSL_USE("ssl.use"),
    SSL_KEYSTORE("ssl.keyStore"),
    SSL_KEYSTORE_PASSWORD("ssl.keyStorePassword"),
    SSL_KEY_ALIAS("ssl.keyAlias"),
    SSL_PORT("ssl.port");
}