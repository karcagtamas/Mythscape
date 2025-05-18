import type { NoteTreeDTO, NoteTreeKey } from '@/models/note'
import { campaignNotesConfig } from '@/requests/campaign.request'
import { get } from '@/utils/requests'
import { defineStore } from 'pinia'

interface NoteState {
  tree: NoteTreeDTO[]
  active: NoteTreeKey | null
  opened: number[]
}

export const useNotesStore = defineStore('notes', {
  state: (): NoteState => ({
    tree: [],
    active: null,
    opened: [],
  }),
  getters: {},
  actions: {
    async fetchNotes(campaignId: number) {
      const response = await get<NoteTreeDTO[]>(campaignNotesConfig(campaignId))

      this.tree = response.data ?? []
    },
    async select(note: NoteTreeKey | null) {
      this.active = note

      if (note !== null) {
        const alreadyOpened = this.opened.find((k) => k === note.id)

        if (alreadyOpened === undefined) {
          this.opened = [...this.opened, note.id]
        }
      }
    },
  },
})
