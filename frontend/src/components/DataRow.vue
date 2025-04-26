<template>
  <div>
    <strong class="text-primary">{{ props.caption }}</strong
    >: {{ value }}
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

type Props = {
  caption: string
  value: string | undefined | null
}

const props = defineProps<Props>()

const value = computed(() => props.value ?? 'N/A')
</script>
