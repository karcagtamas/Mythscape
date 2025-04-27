import { type CampaignNoteDTO } from '@/models/campaign'
import { campaignNotesConfig } from '@/requests/campaign.request'
import { get } from '@/utils/requests'
import { defineStore } from 'pinia'

interface NoteState {
  notes: CampaignNoteDTO[]
  selected: CampaignNoteDTO | null
}

export const useNotesStore = defineStore('notes', {
  state: (): NoteState => ({
    notes: [],
    selected: null,
  }),
  getters: {},
  actions: {
    async fetchNotes(campaignId: number) {
      const response = await get<CampaignNoteDTO[]>(campaignNotesConfig(campaignId))

      this.notes = response.data ?? []
    },
    async select(note: CampaignNoteDTO) {
      this.selected = note
    },
  },
})
