<template>
  <v-navigation-drawer location="right" width="244">
    <v-list v-if="!loading" :lines="false" density="compact" nav color="primary" selectable>
      <template v-slot:prepend="{ item }">
        <v-icon>{{ item.icon }}</v-icon>
      </template>
    </v-list>
    <v-progress-circular v-else color="primary" indeterminate></v-progress-circular>
  </v-navigation-drawer>

  <div></div>
</template>

<script setup lang="ts">
import type { NoteTreeDTO } from '@/models/note'
import { useCampaignStore } from '@/stores/campaign.store'
import { useNotesStore } from '@/stores/notes.store'
import { computed, onMounted, ref } from 'vue'

const loading = ref(false)

const campaignStore = useCampaignStore()
const notesStore = useNotesStore()
const notes = computed<NoteTreeDTO>(() => notesStore.notes)
//const selectedNote = computed<CampaignNoteDTO | null>(() => notesStore.selected)

onMounted(() => {
  if (campaignStore.current?.id) {
    loading.value = true
    notesStore.fetchNotes(campaignStore.current.id)
    loading.value = false
  }
})
</script>
