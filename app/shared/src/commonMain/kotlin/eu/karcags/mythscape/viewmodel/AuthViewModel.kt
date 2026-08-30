package eu.karcags.mythscape.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.common.SessionManager
import eu.karcags.mythscape.dtos.auth.LoginDTO
import eu.karcags.mythscape.dtos.auth.RegisterDTO
import eu.karcags.mythscape.network.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class AuthMode {
    LOGIN,
    REGISTER,
}

class AuthViewModel(
    private val sessionManager: SessionManager,
    private val repository: AuthRepository,
) : ViewModel() {

    var mode by mutableStateOf(AuthMode.LOGIN)
        private set

    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordConfirm by mutableStateOf("")
    var fullName by mutableStateOf("")

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    private val _errors = MutableStateFlow<Map<String, String>>(emptyMap())
    val errors: StateFlow<Map<String, String>> = _errors

    fun toggleAuthMode() {
        mode = if (mode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
        _errors.value = emptyMap()
        password = ""
        passwordConfirm = ""
    }

    fun submit(onAuthSuccess: (String) -> Unit) {
        val isLogin = mode == AuthMode.LOGIN
        val localErrors = mutableMapOf<String, String>()
        if (username.length < 6) localErrors["username"] = "Minimum 6 characters"
        if (password.length < 8) localErrors["password"] = "Minimum 8 characters"

        if (!isLogin) {
            if (!email.contains("@")) localErrors["email"] = "Invalid e-mail format"
            if (password != passwordConfirm) localErrors["passwordConfirm"] = "Passwords mismatch"
            if (fullName.isBlank()) localErrors["fullName"] = "Required"
        }

        if (localErrors.isNotEmpty()) {
            _errors.value = localErrors
            return
        }

        _errors.value = emptyMap()
        _isProcessing.value = true

        viewModelScope.launch {
            try {
                if (isLogin) {
                    val res = repository.login(LoginDTO(username, password))

                    if (res.data != null) {
                        sessionManager.saveSession(
                            token = res.data!!.token,
                            refreshToken = res.data!!.refreshToken,
                            clientId = res.data!!.clientId,
                            userId = res.data!!.user.id,
                            username = res.data!!.user.username,
                            expiresAt = res.data!!.expiresAt,
                        )

                        onAuthSuccess(res.data!!.token)
                    } else {
                        _errors.value = mapOf("global" to (res.error?.message ?: "Invalid credentials"))
                    }
                } else {
                    val res = repository.register(RegisterDTO(username, email, password, passwordConfirm, fullName))
                    if (res.success) {
                        toggleAuthMode()
                    } else {
                        _errors.value = mapOf("global" to (res.error?.message ?: "Registration failed"))
                    }
                }
            } catch (e: Exception) {
                _errors.value = mapOf("global" to (e.message ?: "Server execution failure"))
            } finally {
                _isProcessing.value = false
            }
        }
    }
}