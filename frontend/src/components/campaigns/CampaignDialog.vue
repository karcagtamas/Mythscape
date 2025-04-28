<template>
  <DialogBase :title="title" mode="editor">
    <template v-slot:activator="{ props }">
      <slot :props="props"></slot>
    </template>

    <template v-slot:default> Hello </template>
  </DialogBase>
</template>

<script setup lang="ts">
import DialogBase from '../DialogBase.vue'
import { computed } from 'vue'

type Props = {
  mode: 'create' | 'edit'
}

const props = defineProps<Props>()

const title = computed<string>(() =>
  props.mode === 'create' ? 'Create Campaign' : 'Edit Campaign',
)
</script>
