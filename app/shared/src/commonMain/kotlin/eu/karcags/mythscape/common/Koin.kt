package eu.karcags.mythscape.common

import eu.karcags.mythscape.network.AuthRepository
import eu.karcags.mythscape.viewmodel.AppViewModel
import eu.karcags.mythscape.viewmodel.AuthViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { SessionManager() }
    single { AuthRepository() }

    viewModelOf(::AppViewModel)
    viewModelOf(::AuthViewModel)
}