import type { NoteDTO, NoteTreeDTO } from '@/models/note'
import { campaignNotesConfig } from '@/requests/campaign.request'
import { get } from '@/utils/requests'
import { defineStore } from 'pinia'

interface NoteState {
  notes: NoteTreeDTO
  selected: NoteDTO | null
}

export const useNotesStore = defineStore('notes', {
  state: (): NoteState => ({
    notes: {
      campaignId: 0,
      folders: [],
      notes: [],
    },
    selected: null,
  }),
  getters: {},
  actions: {
    async fetchNotes(campaignId: number) {
      const response = await get<NoteTreeDTO>(campaignNotesConfig(campaignId))

      this.notes = response.data ?? {
        campaignId: campaignId,
        folders: [],
        notes: [],
      }
    },
    async select(note: NoteDTO) {
      this.selected = note
    },
  },
})
