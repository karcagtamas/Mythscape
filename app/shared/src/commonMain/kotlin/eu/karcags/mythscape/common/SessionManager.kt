package eu.karcags.mythscape.common

import com.russhwolf.settings.Settings
import kotlin.time.Clock

class SessionManager {
    private val settings = Settings()

    fun saveSession(
        token: String,
        refreshToken: String,
        clientId: String,
        userId: Int,
        username: String = "",
        expiresAt: Long,
    ) {
        settings.putString("access_token", token)
        settings.putString("refresh_token", refreshToken)
        settings.putInt("user_id", userId)
        settings.putString("username", username)
        settings.putLong("expires_at", expiresAt)
        settings.putString("client_id", clientId)
    }

    fun getAccessToken(): String? = settings.getStringOrNull("access_token")

    fun getRefreshToken(): String? = settings.getStringOrNull("refresh_token")

    fun getUserId(): Int = settings.getInt("user_id", 0)

    fun getUsername(): String? = settings.getStringOrNull("username")

    fun getClientId(): String? = settings.getStringOrNull("client_id")

    fun isAccessTokenExpired(): Boolean {
        val expiresAt = settings.getLong("expires_at", 0L)
        val now = Clock.System.now().epochSeconds

        return now >= expiresAt
    }

    fun clearSession() {
        settings.remove("access_token")
        settings.remove("refresh_token")
        settings.remove("user_id")
        settings.remove("username")
        settings.remove("expires_at")
        settings.remove("client_id")
    }
}