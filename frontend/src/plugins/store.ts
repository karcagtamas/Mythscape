import { type AuthState, type LoginDTO, type TokenDTO } from '@/models/auth'
import type { ServerResponse } from '@/models/response'
import type { UserDTO } from '@/models/user'
import axios from 'axios'
import { createStore } from 'vuex'

const SERVER_URL = 'http://localhost:8080'
const API_URL = `${SERVER_URL}/api`

const store = createStore<AuthState>({
  state: {
    user: null,
    token: localStorage.getItem('token') || '',
  },
  mutations: {
    SET_USER(state, user: UserDTO) {
      state.user = user
      localStorage.setItem('user', JSON.stringify(user))
    },
    SET_TOKEN(state, token: string) {
      state.token = token
      localStorage.setItem('token', token)
    },
    LOGOUT(state) {
      state.user = null
      state.token = ''
      localStorage.removeItem('token')
    },
  },
  actions: {
    async login({ commit }, dto: LoginDTO): Promise<void> {
      try {
        const response = await axios.post<ServerResponse<TokenDTO>>(`${SERVER_URL}/auth/login`, dto)
        const token = response.data.data?.token ?? ''
        commit('SET_TOKEN', token)
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      } catch (error) {
        console.error(error)
        throw error
      }
    },
    async fetchUser({ commit }) {
      try {
        const response = await axios.get<ServerResponse<UserDTO>>(`${API_URL}/users/current`)
        commit('SET_USER', response.data.data)
      } catch (err) {
        console.error(err)
      }
    },
    logout({ commit }): void {
      commit('LOGOUT')
      delete axios.defaults.headers.common['Authorization']
    },
  },
  getters: {
    isAuthenticated: (state) => !!state.token,
    user: (state) => state.user,
  },
})

export default store
