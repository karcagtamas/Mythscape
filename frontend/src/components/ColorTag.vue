<template>
  <v-chip label :color="props.color">{{ props.caption }}</v-chip>
</template>

<script setup lang="ts">
type Props = {
  caption: string
  color: string
}

const props = defineProps<Props>()
</script>
