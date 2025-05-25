import { type AxiosResponse } from 'axios'
import { ref } from 'vue'
import { API_URL } from './constants'
import type { ServerResponse } from '@/models/response'
import { useCommonStore } from '@/stores/common.store'
import axios from '@/plugins/axios'

type QueryParams = Record<
  string,
  string | number | boolean | string[] | number[] | null | undefined
>

export type RequestConfig = {
  type: 'get' | 'post' | 'put' | 'delete'
  pathSegments: string[] | string
  queryParams: QueryParams
}

type ErrorData = {
  message: string
  severity: 'critical' | 'error' | 'warn'
}

const assemblePath = (segments: string[] | string): string => {
  if (typeof segments === 'string') {
    return `${API_URL}/${segments}`
  }

  return [API_URL, ...segments].join('/')
}

export const useAPI = () => {
  const doRequest = async <T>(
    query: () => Promise<ServerResponse<T>>,
  ): Promise<ServerResponse<T>> => {
    const result = await query()

    if (!result) {
      throw Error('Missing response content')
    }

    if (!result.success) {
      console.error(result.error)
      throw Error(result.error?.message)
    }

    return result
  }

  return {
    doRequest,
  }
}

export const fetch = async <T, TBody>(config: RequestConfig, body: TBody | null = null) => {
  const data = ref<T | null>(null)
  const error = ref<ErrorData | null>(null)
  const loading = ref(false)
  const commonStore = useCommonStore()

  const handleError = (errorData: ErrorData): ErrorData => {
    commonStore.setMessage({ text: errorData.message, type: 'error' })
    console.error(errorData)
    return errorData
  }

  try {
    const path = assemblePath(config.pathSegments)
    let response: AxiosResponse<ServerResponse<T>> | null = null
    if (config.type === 'get') {
      response = await axios.get<ServerResponse<T>>(path, { params: config.queryParams })
    } else if (config.type === 'post') {
      response = await axios.post<ServerResponse<T>>(path, body, {
        params: config.queryParams,
      })
    }

    if (response) {
      if (response.data) {
        if (response.data.success) {
          data.value = response.data.data
        } else {
          const responseError = response.data.error!
          console.error(responseError)
          error.value = handleError({ message: responseError.message ?? '', severity: 'error' })
        }
      }
    } else {
      error.value = handleError({
        message: 'Missing response has been detected',
        severity: 'critical',
      })
    }
  } catch (err: Error | unknown) {
    console.error(err)

    let msg = 'Unknow exception'
    if (err instanceof Error) {
      msg = err.message
    }

    error.value = handleError({
      message: msg,
      severity: 'critical',
    })
  } finally {
    loading.value = false
  }
}

export const get = async <T>(config: RequestConfig): Promise<ServerResponse<T>> => {
  return (
    await axios.get<ServerResponse<T>>(assemblePath(config.pathSegments), {
      params: config.queryParams,
    })
  ).data
}

export const post = async <T, TBody>(
  config: RequestConfig,
  body: TBody,
): Promise<ServerResponse<T>> => {
  return (
    await axios.post<ServerResponse<T>>(assemblePath(config.pathSegments), body, {
      params: config.queryParams,
    })
  ).data
}

export const put = async <T, TBody>(
  config: RequestConfig,
  body: TBody,
): Promise<ServerResponse<T>> => {
  return (
    await axios.put<ServerResponse<T>>(assemblePath(config.pathSegments), body, {
      params: config.queryParams,
    })
  ).data
}

export const del = async <T>(config: RequestConfig): Promise<ServerResponse<T>> => {
  return (
    await axios.delete<ServerResponse<T>>(assemblePath(config.pathSegments), {
      params: config.queryParams,
    })
  ).data
}
