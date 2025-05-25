import type { RequestConfig } from '@/utils/requests'

export const sessionsConfig = (campaignId?: number, showAll: boolean = false): RequestConfig => {
  return {
    type: 'get',
    pathSegments: ['sessions'],
    queryParams: { campaignId, showAll },
  }
}
