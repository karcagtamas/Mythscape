import { type CampaignTagDTO, type CampaignDTO, type CampaignMemberDTO } from '@/models/campaign'
import {
  campaignMembersConfig,
  campaignTagsConfig,
  userCampaignsConfig,
} from '@/requests/campaign.request'
import { get } from '@/utils/requests'
import { defineStore } from 'pinia'

interface CampaignState {
  campaigns: CampaignDTO[]
  current: CampaignDTO | null
  page: string

  tags: CampaignTagDTO[]
  members: CampaignMemberDTO[]
}

export const useCampaignStore = defineStore('campaign', {
  state: (): CampaignState => ({
    campaigns: [],
    current: null,
    page: 'data',
    tags: [],
    members: [],
  }),
  getters: {
    entries: (state) => state.campaigns,
  },
  actions: {
    async fetchCampaign(userId: number) {
      const response = await get<CampaignDTO[]>(userCampaignsConfig(userId))

      this.campaigns = response.data ?? []
    },
    async fetchTags(campaignId: number) {
      const response = await get<CampaignTagDTO[]>(campaignTagsConfig(campaignId))

      this.tags = response.data ?? []
    },
    async fetchMembers(campaignId: number) {
      const response = await get<CampaignMemberDTO[]>(campaignMembersConfig(campaignId))

      this.members = response.data ?? []
    },
    async select(campaign: CampaignDTO) {
      this.current = campaign
      this.page = 'data'

      await this.fetchTags(campaign.id)
      await this.fetchMembers(campaign.id)
    },
    setPage(key: string) {
      this.page = key
    },
  },
})
