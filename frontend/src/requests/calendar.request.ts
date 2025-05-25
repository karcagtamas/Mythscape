import type { RequestConfig } from '@/utils/requests'

export const sessionsConfig = (campaignId?: number, showAll: boolean = false): RequestConfig => {
  return {
    type: 'get',
    pathSegments: ['sessions'],
    queryParams: { campaignId, showAll },
  }
}

export const sessionCreateConfig = (): RequestConfig => {
  return {
    type: 'post',
    pathSegments: ['sessions'],
    queryParams: {},
  }
}

export const sessionEditConfig = (sessionId: number): RequestConfig => {
  return {
    type: 'put',
    pathSegments: ['sessions', sessionId.toString()],
    queryParams: {},
  }
}

export const sessionDeleteConfig = (sessionId: number): RequestConfig => {
  return {
    type: 'delete',
    pathSegments: ['sessions', sessionId.toString()],
    queryParams: {},
  }
}
