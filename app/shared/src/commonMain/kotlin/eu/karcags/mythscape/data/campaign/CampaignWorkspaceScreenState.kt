package eu.karcags.mythscape.data.campaign

sealed class CampaignWorkspaceScreenState {

    data object Dashboard : CampaignWorkspaceScreenState()
    data object Sessions : CampaignWorkspaceScreenState()
    data object Tags : CampaignWorkspaceScreenState()
    data object Members : CampaignWorkspaceScreenState()
    data object Notes : CampaignWorkspaceScreenState()
}