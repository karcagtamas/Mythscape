<template>
  <v-treeview
    :items="props.tree"
    density="compact"
    item-value="key"
    item-title="name"
    :activated="props.active"
    activatable
    color="primary"
    active-strategy="single-leaf"
    expand-icon="mdi-chevron-right"
    collapse-icon="mdi-chevron-down"
    open-on-click
    fluid
    @update:activated="(e) => handleActivate(e as NoteTreeKey[])"
  >
    <template v-slot:prepend="{ item, isOpen }">
      <v-icon
        v-if="item.key.type === 'FOLDER'"
        :icon="isOpen ? 'mdi-folder-open' : 'mdi-folder'"
      ></v-icon>
      <v-icon v-else icon="mdi-note"></v-icon>
    </template>
  </v-treeview>
</template>

<script setup lang="ts">
import type { NoteTreeDTO, NoteTreeKey } from '@/models/note'
import { defineProps } from 'vue'

type Props = {
  tree: NoteTreeDTO[]
  active: NoteTreeKey | null
}

const props = defineProps<Props>()
const emits = defineEmits({
  activated(key: NoteTreeKey | null) {
    return true
  },
})

const handleActivate = (keys: NoteTreeKey[]) => {
  emits('activated', keys.length ? keys[0] : null)
}
</script>
