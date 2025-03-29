import type { TokenDTO } from '@/models/auth'
import type { UserDTO } from '@/models/user'
import { currentUserConfig } from '@/requests/user.request'
import { get } from '@/utils/requests'
import axios from 'axios'
import { defineStore } from 'pinia'

interface AuthState {
  user: UserDTO | null
  token: string
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    user: JSON.parse(localStorage.getItem('user') ?? 'null') || null,
    token: localStorage.getItem('token') || '',
  }),
  getters: {
    currentUser: (state) => state.user,
    loggedIn: (state) => !!state.token,
  },
  actions: {
    login(token: TokenDTO) {
      this.setToken(token.token)
      axios.defaults.headers.common['Authorization'] = `Bearer ${token.token}`
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
    logout() {
      this.$reset()
      delete axios.defaults.headers.common['Authorization']
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },
  },
})
