package eu.karcags.mythscape.enums

import eu.karcags.mythscape.dtos.campaigns.CampaignDTO

sealed class WorkspaceScreenState {

    data object Dashboard : WorkspaceScreenState()

    data object Profile : WorkspaceScreenState()

    class CampaignWorkspace(val campaign: CampaignDTO) : WorkspaceScreenState()
}