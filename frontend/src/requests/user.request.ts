import type { RequestConfig } from '@/utils/requests'

export const currentUserConfig = (): RequestConfig => {
  return {
    type: 'get',
    pathSegments: ['users', 'current'],
    queryParams: [],
  }
}
