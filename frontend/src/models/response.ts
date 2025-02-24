interface ErrorData {
  message?: string
  stackTrace: string[]
  subMessages: string[]
}

export interface ServerResponse<T> {
  data?: T
  statusCode: number
  success: boolean
  error?: ErrorData
}
