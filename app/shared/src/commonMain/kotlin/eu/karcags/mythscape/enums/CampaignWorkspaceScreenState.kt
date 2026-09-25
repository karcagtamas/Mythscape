package eu.karcags.mythscape.enums

sealed class CampaignWorkspaceScreenState {

    data object Dashboard : CampaignWorkspaceScreenState()
    data object Sessions : CampaignWorkspaceScreenState()
    data object Notes : CampaignWorkspaceScreenState()
}