import { type CampaignTagDTO, type CampaignDTO, type CampaignMemberDTO } from '@/models/campaign'
import {
  campaignConfig,
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
    async fetchCampaigns(userId: number) {
      const response = await get<CampaignDTO[]>(userCampaignsConfig(userId))

      this.campaigns = response.data ?? []

      if (this.current) {
        this.current = this.campaigns.find((c) => c.id === this.current?.id) ?? null
      }
    },
    async fetchTags(campaignId: number) {
      const response = await get<CampaignTagDTO[]>(campaignTagsConfig(campaignId))

      this.tags = response.data ?? []
    },
    async fetchMembers(campaignId: number) {
      const response = await get<CampaignMemberDTO[]>(campaignMembersConfig(campaignId))

      this.members = response.data ?? []
    },
    async selectById(campaignId: number, page: string = 'data') {
      const response = await get<CampaignDTO | null>(campaignConfig(campaignId))

      if (response.data) {
        await this.select(response.data, page)
      }
    },
    async select(campaign: CampaignDTO, page: string = 'data') {
      this.current = campaign
      this.page = page

      await this.fetchTags(campaign.id)
      await this.fetchMembers(campaign.id)
    },
    deselect() {
      this.current = null
      this.page = 'data'
      this.tags = []
      this.members = []
    },
    setPage(key: string) {
      this.page = key
    },
  },
})
