<template>
  <v-dialog>
    <template v-slot:activator="{ props: activatorProps }">
      <slot name="activator" :props="activatorProps"></slot>
    </template>

    <template v-slot:default="{ isActive }">
      <v-card>
        <v-card-title class="text-primary">{{ props.title }}</v-card-title>
        <v-card-text>
          <slot :isActive="isActive"></slot>
        </v-card-text>
        <v-card-actions>
          <v-btn @click="isActive.value = false">{{ closeCaption }}</v-btn>
          <v-btn
            v-if="submitCaption"
            color="primary"
            :disabled="props.submitDisabled"
            @click="emit('submit', isActive)"
            >{{ submitCaption }}</v-btn
          >
        </v-card-actions>
      </v-card>
    </template>
  </v-dialog>
</template>

<script setup lang="ts">
import { computed, type Ref } from 'vue'

type Props = {
  title: string
  mode: 'info' | 'confirm' | 'editor'
  submitDisabled: boolean
}

const props = defineProps<Props>()
const emit = defineEmits({
  submit(isActive: Ref<boolean, boolean>): boolean {
    return true
  },
})

const closeCaption = computed<string>(() => {
  if (props.mode === 'editor') {
    return 'Cancel'
  }

  if (props.mode === 'confirm') {
    return 'No'
  }

  return 'Close'
})

const submitCaption = computed<string>(() => {
  if (props.mode === 'editor') {
    return 'Submit'
  }

  if (props.mode === 'confirm') {
    return 'Yes'
  }

  return ''
})
</script>
