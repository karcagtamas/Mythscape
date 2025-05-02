<template>
  <v-chip label :color="props.color" :closable="closable" @click:close="emit('close')">{{
    props.caption
  }}</v-chip>
</template>

<script setup lang="ts">
type Props = {
  caption: string
  color: string
  closable: boolean
}

const props = defineProps<Props>()
const emit = defineEmits({
  close() {
    return true
  },
})
</script>
