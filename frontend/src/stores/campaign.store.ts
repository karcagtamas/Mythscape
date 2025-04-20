import type { CampaignDTO } from '@/models/campaign'
import { userCampaignsConfig } from '@/requests/campaign.request'
import { get } from '@/utils/requests'
import { defineStore } from 'pinia'

interface CampaignState {
  campaigns: CampaignDTO[]
  current: CampaignDTO | null
  page: string
}

export const useCampaignStore = defineStore('campaign', {
  state: (): CampaignState => ({
    campaigns: [],
    current: null,
    page: 'data',
  }),
  getters: {
    entries: (state) => state.campaigns,
  },
  actions: {
    async fetchCampaign(userId: number) {
      const response = await get<CampaignDTO[]>(userCampaignsConfig(userId))

      this.campaigns = response.data ?? []
    },
    select(campaign: CampaignDTO) {
      this.current = campaign
      this.page = 'data'
    },
    setPage(key: string) {
      this.page = key
    },
  },
})
