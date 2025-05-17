<template>
  <v-navigation-drawer location="right" width="244">
    <NoteTreeList
      v-if="tree.length"
      :tree="tree"
      :active="activatedNote"
      @activated="handleActivate"
    ></NoteTreeList>
    <v-progress-circular v-else color="primary" indeterminate></v-progress-circular>
  </v-navigation-drawer>

  <div></div>
</template>

<script setup lang="ts">
import type { NoteTreeDTO, NoteTreeKey } from '@/models/note'
import { useCampaignStore } from '@/stores/campaign.store'
import { useNotesStore } from '@/stores/notes.store'
import { computed, onMounted, ref } from 'vue'
import NoteTreeList from '@/components/campaigns/NoteTreeList.vue'
import { useRouter } from 'vue-router'

const loading = ref(false)

const campaignStore = useCampaignStore()
const notesStore = useNotesStore()
const router = useRouter()

const tree = computed<NoteTreeDTO[]>(() => notesStore.tree)
const activatedNote = computed<NoteTreeKey | null>(() => notesStore.selected)

onMounted(() => {
  if (campaignStore.current?.id) {
    loading.value = true
    notesStore.fetchNotes(campaignStore.current.id)
    loading.value = false
  }
})

const handleActivate = (key: NoteTreeKey | null) => {
  notesStore.select(key)
  console.log(key)
  router.push(`/app/campaigns/${campaignStore.current?.id}/notes/${key?.id}`)
}
</script>
