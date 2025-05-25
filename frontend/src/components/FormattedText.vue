<template>{{ value }}</template>

<script setup lang="ts">
import { format } from 'date-fns'
import { computed } from 'vue'

type Props = {
  value: Date | undefined | null
  format: 'date' | 'date-time'
}

const props = defineProps<Props>()
const value = computed(() => {
  const formatStr = props.format === 'date' ? 'yyyy-MM-dd' : 'yyyy-MM-dd HH:mm:ss'
  return props.value ? format(props.value, formatStr) : null
})
</script>
