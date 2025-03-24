import type { LoginDTO, TokenDTO } from '@/models/auth'
import type { ServerResponse } from '@/models/response'
import type { UserDTO } from '@/models/user'
import axios from 'axios'
import { defineStore } from 'pinia'

const SERVER_URL = 'http://localhost:8080'
const API_URL = `${SERVER_URL}/api`

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
    user: (state) => state.user,
    loggedIn: (state) => !!state.token,
  },
  actions: {
    async login(payload: LoginDTO) {
      try {
        const response = await axios.post<ServerResponse<TokenDTO>>(
          `${SERVER_URL}/auth/login`,
          payload,
        )
        const token = response.data.data?.token ?? ''
        this.setToken(token)
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      } catch (error) {
        console.error(error)
        throw error
      }
    },
    async fetchUser() {
      try {
        const response = await axios.get<ServerResponse<UserDTO>>(`${API_URL}/users/current`)
        if (response.data.data) {
          this.setUser(response.data.data)
        }
      } catch (err) {
        console.error(err)
      }
    },
    setUser(payload: UserDTO) {
      this.user = payload
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
