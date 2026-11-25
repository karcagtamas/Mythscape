package eu.karcags.mythscape.data

sealed class WorkspaceScreenState {

    data object Dashboard : WorkspaceScreenState()

    data object Profile : WorkspaceScreenState()

    data object Calendar : WorkspaceScreenState()

    class CampaignWorkspace(val campaignId: Int, val title: String) : WorkspaceScreenState()
}