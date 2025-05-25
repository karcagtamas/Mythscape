export interface SessionDTO {
  id: number
  date: Date
  startTime: string
  endTime: string
}

export interface SessionEditDTO {
  date: Date
  startTime: string
  endTime: string
  campaignId: number
}
