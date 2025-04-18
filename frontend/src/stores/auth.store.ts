import type { TokenDTO } from '@/models/auth'
import type { UserDTO } from '@/models/user'
import { logoutConfig } from '@/requests/auth.request'
import { currentUserConfig } from '@/requests/user.request'
import { get, post } from '@/utils/requests'
import axios from 'axios'
import { defineStore } from 'pinia'

interface AuthState {
  user: UserDTO | null
  token: string
  clientId: string
  refreshToken: string
  userId: number
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    user: JSON.parse(localStorage.getItem('user') ?? 'null') || null,
    token: localStorage.getItem('token') || '',
    clientId: localStorage.getItem('clientId') || '',
    refreshToken: localStorage.getItem('refreshToken') || '',
    userId: JSON.parse(localStorage.getItem('userId') ?? '0'),
  }),
  getters: {
    currentUser: (state) => state.user,
    loggedIn: (state) => !!state.token,
  },
  actions: {
    login(dto: TokenDTO) {
      this.setToken(dto.token)
      this.setClientId(dto.clientId)
      this.setRefreshToken(dto.refreshToken)
      this.setUserId(dto.user.id)
      axios.defaults.headers.common['Authorization'] = `Bearer ${dto.token}`
    },
    async fetchUser() {
      try {
        const response = await get<UserDTO>(currentUserConfig())
        if (response.data) {
          this.setUser(response.data)
        }
      } catch (err) {
        console.error(err)
      }
    },
    setUser(payload: UserDTO) {
      this.user = { ...payload }
      localStorage.setItem('user', JSON.stringify(payload))
    },
    setToken(payload: string) {
      this.token = payload
      localStorage.setItem('token', payload)
    },
    setClientId(payload: string) {
      this.clientId = payload
      localStorage.setItem('clientId', payload)
    },
    setRefreshToken(payload: string) {
      this.refreshToken = payload
      localStorage.setItem('refreshToken', payload)
    },
    setUserId(payload: number) {
      this.userId = payload
      localStorage.setItem('userId', payload.toString())
    },
    async logout() {
      await post<void, unknown>(logoutConfig(), { userId: this.userId, clientId: this.clientId })
      this.$reset()
      delete axios.defaults.headers.common['Authorization']
      localStorage.removeItem('token')
      localStorage.removeItem('clientId')
      localStorage.removeItem('refreshToken')
      localStorage.removeItem('userId')
      localStorage.removeItem('user')
    },
  },
})
