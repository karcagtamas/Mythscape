import type { TokenDTO } from '@/models/auth'
import type { ServerResponse } from '@/models/response'
import { useAuthStore } from '@/stores/auth.store'
import { createFetch } from '@vueuse/core'
import { useRouter } from 'vue-router'

let lastRequestOptions: RequestInit = {}

const executeTokenRefresh = async (): Promise<string> => {
  const authStore = useAuthStore()

  try {
    const rawResponse = await fetch('http://localhost:8080/api/auth/refresh', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        clientId: localStorage.getItem('clientId'),
        refreshToken: localStorage.getItem('refreshToken'),
        userId: localStorage.getItem('userId'),
      }),
    })

    if (!rawResponse.ok) throw new Error('Refresh token invalid')

    const responseData: ServerResponse<TokenDTO> = await rawResponse.json()

    if (responseData.data) {
      authStore.setToken(responseData.data.token)
      authStore.setRefreshToken(responseData.data.refreshToken)
      return responseData.data.token
    }
  } catch (err) {
    console.error('[Auth Engine] Persistent token failure: ', err)
  }

  await authStore.logout()
  const router = useRouter()
  if (router) {
    router.replace('/auth/login')
  } else {
    window.location.href = '/auth/login'
  }

  return ''
}

export const useApi = createFetch({
  baseUrl: 'http://localhost:8080/api',
  options: {
    async beforeFetch({ options }) {
      const token = localStorage.getItem('token')

      options.headers = {
        ...options.headers,
        'Content-Type': 'application/json',
        Accept: 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      }

      lastRequestOptions = options
      return { options }
    },

    async onFetchError(ctx) {
      if (!ctx.response || ctx.response.url.endsWith('/auth/refresh')) return ctx

      if (ctx.response.status === 401) {
        const newToken = await executeTokenRefresh()

        if (newToken) {
          // Update authorization headers on our cached request object
          const headers = (lastRequestOptions.headers || {}) as Record<string, string>
          headers['Authorization'] = `Bearer ${newToken}`
          lastRequestOptions.headers = headers

          // Re-fire the failed request using native fetch, pulling the URL from the response log
          const retryResponse = await fetch(ctx.response.url, lastRequestOptions)

          // Reassign the updated context payload fields back to VueUse's active refs
          ctx.data = await retryResponse.json()
          ctx.response = retryResponse
          ctx.error = null // Resets the hook state to success
        }
      }

      return ctx
    },
  },
  fetchOptions: {
    mode: 'cors',
  },
})
