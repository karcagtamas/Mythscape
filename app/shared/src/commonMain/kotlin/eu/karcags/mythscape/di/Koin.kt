package eu.karcags.mythscape.di

import eu.karcags.mythscape.network.AuthRepository
import eu.karcags.mythscape.viewmodel.AuthViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { AuthRepository() }
    viewModelOf(::AuthViewModel)
}