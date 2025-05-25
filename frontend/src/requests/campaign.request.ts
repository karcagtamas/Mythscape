import type { RequestConfig } from '@/utils/requests'

export const userCampaignsConfig = (userId: number): RequestConfig => {
  return {
    type: 'get',
    pathSegments: ['campaigns', 'user', userId.toString()],
    queryParams: {},
  }
}

export const campaignConfig = (campaignId: number): RequestConfig => {
  return {
    type: 'get',
    pathSegments: ['campaigns', campaignId.toString()],
    queryParams: {},
  }
}

export const campaignCreateConfig = (): RequestConfig => {
  return {
    type: 'post',
    pathSegments: ['campaigns'],
    queryParams: {},
  }
}

export const campaignEditConfig = (campaignId: number): RequestConfig => {
  return {
    type: 'put',
    pathSegments: ['campaigns', campaignId.toString()],
    queryParams: {},
  }
}

export const campaignDeleteConfig = (campaignId: number): RequestConfig => {
  return {
    type: 'delete',
    pathSegments: ['campaigns', campaignId.toString()],
    queryParams: {},
  }
}

export const campaignTagsConfig = (campaignId: number): RequestConfig => {
  return {
    type: 'get',
    pathSegments: ['campaigns', campaignId.toString(), 'tags'],
    queryParams: {},
  }
}

export const campaignTagCreateConfig = (campaignId: number): RequestConfig => {
  return {
    type: 'post',
    pathSegments: ['campaigns', campaignId.toString(), 'tags'],
    queryParams: {},
  }
}

export const campaignTagDeleteConfig = (campaignId: number, tagId: number): RequestConfig => {
  return {
    type: 'delete',
    pathSegments: ['campaigns', campaignId.toString(), 'tags', tagId.toString()],
    queryParams: {},
  }
}

export const campaignMembersConfig = (campaignId: number): RequestConfig => {
  return {
    type: 'get',
    pathSegments: ['campaigns', campaignId.toString(), 'members'],
    queryParams: {},
  }
}

export const campaignNotesConfig = (campaignId: number): RequestConfig => {
  return {
    type: 'get',
    pathSegments: ['campaigns', campaignId.toString(), 'notes'],
    queryParams: {},
  }
}
