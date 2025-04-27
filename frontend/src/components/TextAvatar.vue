<template>
  <v-avatar :class="props.class" :size="props.size" :color="color"
    ><span
      :style="`height: ${props.size}px; width: ${props.size}.px; line-height: ${props.size}px; font-size: ${textSize}px`"
      >{{ value }}</span
    ></v-avatar
  >
</template>

<script setup lang="ts">
import { computed } from 'vue'

type Props = {
  value: string | undefined
  color: string
  class?: unknown
  size?: string | number | undefined
  textSize?: number
}

const props = defineProps<Props>()
const value = computed(() =>
  (props.value ?? '')
    .split(' ')
    .filter((e) => e.length > 0)
    .map((e) => e[0])
    .slice(0, 2)
    .join('')
    .toUpperCase(),
)
const textSize = computed(() => props.textSize ?? 16)
</script>
