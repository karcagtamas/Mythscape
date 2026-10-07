package eu.karcags.mythscape.enums

import eu.karcags.mythscape.dtos.campaigns.CampaignDTO

sealed class WorkspaceScreenState {

    data object Dashboard : WorkspaceScreenState()

    data object Profile : WorkspaceScreenState()

    data object Calendar : WorkspaceScreenState()

    class CampaignWorkspace(val campaignId: Int, val title: String) : WorkspaceScreenState()
}