import type { RequestConfig } from '@/utils/requests'

export const userCampaignsConfig = (userId: number): RequestConfig => {
  return {
    type: 'get',
    pathSegments: ['campaigns', 'user', userId.toString()],
    queryParams: [],
  }
}
