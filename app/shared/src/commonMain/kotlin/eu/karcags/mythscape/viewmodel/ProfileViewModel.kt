package eu.karcags.mythscape.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.karcags.mythscape.network.UserRepository
import eu.karcags.mythscape.utils.formatted
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    var username by mutableStateOf("N/A")
    var email by mutableStateOf("N/A")
    var fullname by mutableStateOf("N/A")
    var registration by mutableStateOf("N/A")

    var isLoading by mutableStateOf(false)
    var globalError by mutableStateOf<String?>(null)

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            isLoading = true
            globalError = null

            try {
                val res = repository.getCurrentUser()
                if (res.success && res.data != null) {
                    val user = res.data!!
                    username = user.username
                    email = user.email
                    fullname = user.name
                    registration = user.register.formatted()
                } else {
                    globalError = res.error?.message ?: "Failed to resolve profile data."
                }
            } catch (e: Exception) {
                globalError = e.message ?: "Network execution timeout channel drop error."
            } finally {
                isLoading = false
            }
        }
    }
}