package eu.karcags.mythscape.common

import eu.karcags.mythscape.network.AuthRepository
import eu.karcags.mythscape.network.CampaignRepository
import eu.karcags.mythscape.viewmodel.AppViewModel
import eu.karcags.mythscape.viewmodel.AuthViewModel
import eu.karcags.mythscape.viewmodel.WorkspaceViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { SessionManager() }
    single { AuthRepository() }
    single { CampaignRepository() }

    viewModelOf(::AppViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::WorkspaceViewModel)
}