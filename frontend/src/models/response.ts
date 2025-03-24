interface ErrorData {
  message?: string
  stackTrace: string[]
  subMessages: string[]
}

export interface ServerResponse<T> {
  data: T | null
  statusCode: number
  success: boolean
  error?: ErrorData
}
