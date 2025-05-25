import type { RequestConfig } from '@/utils/requests'

export const loginConfig = (): RequestConfig => {
  return {
    type: 'post',
    pathSegments: ['auth', 'login'],
    queryParams: {},
  }
}

export const registerConfig = (): RequestConfig => {
  return {
    type: 'post',
    pathSegments: ['auth', 'register'],
    queryParams: {},
  }
}

export const logoutConfig = (): RequestConfig => {
  return {
    type: 'post',
    pathSegments: ['auth', 'logout'],
    queryParams: {},
  }
}
