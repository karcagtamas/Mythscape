import type { TokenDTO } from '@/models/auth'
import type { ServerResponse } from '@/models/response'
import { useAuthStore } from '@/stores/auth.store'
import axios from 'axios'
import { useRouter } from 'vue-router'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

const refreshToken = async () => {
  const response = await axios.post<ServerResponse<TokenDTO>>(
    'http://localhost:8080/api/auth/refresh',
    {
      clientId: localStorage.getItem('clientId'),
      refreshToken: localStorage.getItem('refreshToken'),
      userId: localStorage.getItem('userId'),
    },
  )

  const authStore = useAuthStore()
  if (response.data.data) {
    authStore.setToken(response.data.data.token)
    authStore.setRefreshToken(response.data.data.refreshToken)
    return response.data.data.token
  }

  await authStore.logout()
  const router = useRouter()
  router.replace('/auth/login')

  return ''
}

api.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const originalRequest = error.config
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true
      const newToken = await refreshToken()

      if (newToken) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${newToken}`
        return api(originalRequest)
      }
    }
    return Promise.reject(error)
  },
)

export default api
