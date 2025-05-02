import type { UserDTO } from './user'

export interface CampaignDTO {
  id: number
  name: string
  title: string
  imageId: number | null
  description: string | null
  creator: UserDTO
  creation: Date
  lastUpdate: Date
}

export interface CampaignEditDTO {
  name: string
  title: string
  description: string | null
}

export interface CampaignTagDTO {
  id: number
  caption: string
  color: string
  creation: Date
}

export interface CampaignTagEditDTO {
  caption: string
  color: string
}

export interface CampaignMemberDTO {
  id: number
  name: string
  campaignId: number
  user: UserDTO | null
  creation: Date
  isDM: boolean
}

export interface CampaignNoteDTO {
  id: number
}
