import type { NoteTreeDTO, NoteTreeKey } from '@/models/note'
import { campaignNotesConfig } from '@/requests/campaign.request'
import { get } from '@/utils/requests'
import { defineStore } from 'pinia'

interface NoteState {
  tree: NoteTreeDTO[]
  selected: NoteTreeKey | null
}

export const useNotesStore = defineStore('notes', {
  state: (): NoteState => ({
    tree: [],
    selected: null,
  }),
  getters: {},
  actions: {
    async fetchNotes(campaignId: number) {
      const response = await get<NoteTreeDTO[]>(campaignNotesConfig(campaignId))

      this.tree = response.data ?? []
    },
    async select(note: NoteTreeKey | null) {
      this.selected = note
    },
  },
})
