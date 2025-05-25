<template>
  <v-navigation-drawer location="right" width="244">
    <NoteTreeList
      v-if="tree.length"
      :tree="tree"
      :active="activateNote"
      @activated="handleActivate"
    ></NoteTreeList>
    <v-progress-circular v-else color="primary" indeterminate></v-progress-circular>
  </v-navigation-drawer>

  <v-card v-if="opened.length" class="note-editor-frame">
    <v-tabs color="secondary" density="compact" show-arrows v-model="current">
      <v-tab v-for="item in opened" v-bind:key="item" :value="item"> Note {{ item }} </v-tab>
    </v-tabs>

    <vue-monaco-editor
      v-if="current"
      theme="vs"
      languge="markdown"
      v-model="code"
      :options="MONACO_EDITOR_OPTIONS"
      @mount="handleMount"
    ></vue-monaco-editor>
  </v-card>
</template>

<script setup lang="ts">
import type { NoteTreeDTO, NoteTreeKey } from '@/models/note'
import { useCampaignStore } from '@/stores/campaign.store'
import { useNotesStore } from '@/stores/notes.store'
import { computed, onMounted, ref, shallowRef } from 'vue'
import NoteTreeList from '@/components/campaigns/NoteTreeList.vue'

const MONACO_EDITOR_OPTIONS = {
  automaticLayout: true,
  formatOnType: true,
  formatOnPaste: true,
}

const loading = ref(false)
const current = ref<number | null>(0)
const code = ref('// some code...')
const editor = shallowRef()

const campaignStore = useCampaignStore()
const notesStore = useNotesStore()

const tree = computed<NoteTreeDTO[]>(() => notesStore.tree)
const activateNote = computed<NoteTreeKey | null>(() => notesStore.active)
const opened = computed<number[]>(() => notesStore.opened)

onMounted(async () => {
  if (campaignStore.current?.id) {
    loading.value = true
    await notesStore.fetchNotes(campaignStore.current.id)
    loading.value = false
  }
})

const handleActivate = (key: NoteTreeKey | null) => {
  notesStore.select(key)
  current.value = key?.id ?? null
}

const handleMount = (editorInstance: unknown) => (editor.value = editorInstance)
</script>

<style lang="scss">
.note-editor-frame {
  flex: 1;
  overflow: hidden;
}
</style>
