import type { SnackbarMessage } from '@/utils/snackbars'
import { defineStore } from 'pinia'

interface CommonState {
  snackMessage: SnackbarMessage | null
  timeout: number | null
}

export const useCommonStore = defineStore('common', {
  state: (): CommonState => ({
    snackMessage: null,
    timeout: null,
  }),
  getters: {
    message: (state) => state.snackMessage,
  },
  actions: {
    setMessage(payload: SnackbarMessage) {
      if (this.timeout !== null) {
        clearTimeout(this.timeout)
      }

      this.snackMessage = payload
      this.timeout = setTimeout(() => {
        this.snackMessage = null
        this.timeout = null
      }, 2000)
    },
  },
})
