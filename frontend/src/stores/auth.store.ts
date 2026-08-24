import type { TokenDTO } from '@/models/auth'
import type { UserDTO } from '@/models/user'
import { useApi } from '@/plugins/api'
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
    user: JSON.parse(localStorage.getItem('user') ?? 'null'),
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

      if (dto.user) {
        this.setUser(dto.user)
      }
    },
    async fetchUser() {
      const { data, error } = await useApi('/users/current').get().json<UserDTO>()

      if (!error.value && data.value) {
        this.setUser(data.value)
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
      try {
        await useApi('/auth/logout').post({
          userId: this.userId,
          clientId: this.clientId,
        })
      } catch (err) {
        console.error('[Auth Store] Server logout route trace failed:', err)
      } finally {
        this.$reset()
        localStorage.removeItem('token')
        localStorage.removeItem('clientId')
        localStorage.removeItem('refreshToken')
        localStorage.removeItem('userId')
        localStorage.removeItem('user')
      }
    },
  },
})
