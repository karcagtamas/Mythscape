package eu.karcags.mythscape.enums

import eu.karcags.mythscape.dtos.campaigns.CampaignDTO

sealed class WorkspaceScreenState {

    data object Dashboard : WorkspaceScreenState()

    data object Profile : WorkspaceScreenState()

    class CampaignDashboard(val campaign: CampaignDTO) : WorkspaceScreenState()

    data object CampaignCreate : WorkspaceScreenState()

    class CampaignEdit(val campaign: CampaignDTO) : WorkspaceScreenState()
}