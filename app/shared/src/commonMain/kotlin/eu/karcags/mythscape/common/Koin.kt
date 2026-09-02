package eu.karcags.mythscape.common

import eu.karcags.mythscape.network.AuthRepository
import eu.karcags.mythscape.network.CampaignRepository
import eu.karcags.mythscape.network.HttpClientFactory
import eu.karcags.mythscape.network.NetworkConfig
import eu.karcags.mythscape.network.SessionRepository
import eu.karcags.mythscape.network.UserRepository
import eu.karcags.mythscape.viewmodel.AppViewModel
import eu.karcags.mythscape.viewmodel.AuthViewModel
import eu.karcags.mythscape.viewmodel.CampaignDashboardViewModel
import eu.karcags.mythscape.viewmodel.CampaignFormViewModel
import eu.karcags.mythscape.viewmodel.ProfileViewModel
import eu.karcags.mythscape.viewmodel.WorkspaceViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { NetworkConfig() }
    single { SessionManager() }

    single { HttpClientFactory.create(get(), get()) }

    single { AuthRepository(get()) }
    single { CampaignRepository(get()) }
    single { UserRepository(get()) }
    single { SessionRepository(get()) }

    viewModelOf(::AppViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::WorkspaceViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::CampaignDashboardViewModel)
    viewModelOf(::CampaignFormViewModel)
}