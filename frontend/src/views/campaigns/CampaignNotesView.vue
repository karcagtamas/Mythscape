<template>
  <v-navigation-drawer location="right" width="244">
    <v-list :lines="false" density="compact" nav color="primary" selectable>
      <template v-slot:prepend="{ item }">
        <v-icon>{{ item.icon }}</v-icon>
      </template>
    </v-list>
  </v-navigation-drawer>

  <div></div>
</template>

<script setup lang="ts">
import type { CampaignNoteDTO } from '@/models/campaign'
import { useNotesStore } from '@/stores/notes.store'
import { computed } from 'vue'

const notesStore = useNotesStore()
const notes = computed<CampaignNoteDTO[]>(() => notesStore.notes)
const selectedNote = computed<CampaignNoteDTO | null>(() => notesStore.selected)
</script>
