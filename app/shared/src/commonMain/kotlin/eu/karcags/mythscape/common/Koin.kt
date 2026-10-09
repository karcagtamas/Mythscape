package eu.karcags.mythscape.common

import eu.karcags.mythscape.network.*
import eu.karcags.mythscape.viewmodel.*
import eu.karcags.mythscape.viewmodel.calendar.AgendaViewModel
import eu.karcags.mythscape.viewmodel.calendar.CalendarViewModel
import eu.karcags.mythscape.viewmodel.calendar.MonthlyCalendarViewModel
import eu.karcags.mythscape.viewmodel.calendar.WeeklyCalendarViewModel
import eu.karcags.mythscape.viewmodel.campaign.CampaignDashboardViewModel
import eu.karcags.mythscape.viewmodel.campaign.CampaignSessionsViewModel
import eu.karcags.mythscape.viewmodel.campaign.CampaignWorkspaceViewModel
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
    viewModelOf(::CalendarViewModel)
    viewModelOf(::AgendaViewModel)
    viewModelOf(::WeeklyCalendarViewModel)
    viewModelOf(::MonthlyCalendarViewModel)
    viewModelOf(::CampaignWorkspaceViewModel)
    viewModelOf(::CampaignDashboardViewModel)
    viewModelOf(::CampaignSessionsViewModel)
}