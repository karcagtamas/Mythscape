import { defineStore } from 'pinia'

interface CommonState {
  snackMessage: string | null
}

export const useCommonStore = defineStore('common', {
  state: (): CommonState => ({
    snackMessage: null,
  }),
  getters: {
    message: (state) => state.snackMessage,
  },
  actions: {
    setMessage(payload: string) {
      this.snackMessage = payload
    },
  },
})
