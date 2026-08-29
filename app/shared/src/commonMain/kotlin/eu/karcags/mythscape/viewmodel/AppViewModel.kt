package eu.karcags.mythscape.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.common.SessionManager
import eu.karcags.mythscape.dtos.auth.LogoutDTO
import eu.karcags.mythscape.dtos.auth.RefreshDTO
import eu.karcags.mythscape.network.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class AppState {
    INITIALIZING,
    UNAUTHENTICATED,
    AUTHENTICATED,
}

class AppViewModel(
    private val sessionManager: SessionManager,
    private val repository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AppState.INITIALIZING)
    val state: StateFlow<AppState> = _state

    init {
        checkAuthenticationLifecycle()
    }

    fun checkAuthenticationLifecycle() {
        viewModelScope.launch {
            val token = sessionManager.getAccessToken()
            val refreshToken = sessionManager.getRefreshToken()
            val clientId = sessionManager.getClientId()

            if (token != null && !sessionManager.isAccessTokenExpired()) {
                _state.value = AppState.AUTHENTICATED
            } else if (refreshToken != null && clientId != null) {
                try {
                    val userId = sessionManager.getUserId()
                    val response = repository.refreshToken(RefreshDTO(refreshToken, clientId, userId))

                    if (response.data != null) {
                        sessionManager.saveSession(
                            response.data!!.token,
                            response.data!!.refreshToken,
                            response.data!!.clientId,
                            response.data!!.user.id,
                            response.data!!.user.username,
                            response.data!!.expiresAt,
                        )

                        _state.value = AppState.AUTHENTICATED
                    } else {
                        sessionManager.clearSession()
                        _state.value = AppState.UNAUTHENTICATED
                    }
                } catch (e: Exception) {
                    sessionManager.clearSession()
                    _state.value = AppState.UNAUTHENTICATED
                }
            } else {
                sessionManager.clearSession()
                _state.value = AppState.UNAUTHENTICATED
            }
        }
    }

    fun setAuthenticated() {
        _state.value = AppState.AUTHENTICATED
    }

    fun logout() {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            val clientId = sessionManager.getClientId()

            if (userId != 0 && clientId != null) {
                repository.logout(LogoutDTO(userId, clientId))
            }

            sessionManager.clearSession()
            _state.value = AppState.UNAUTHENTICATED
        }
    }
}