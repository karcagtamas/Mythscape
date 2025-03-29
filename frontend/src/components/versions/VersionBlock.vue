<template>
  <v-card>
    <v-card-title class="text-primary font-weight-bold"
      >Version {{ props.version.release }}</v-card-title
    >
    <v-card-subtitle>{{ date }}</v-card-subtitle>
    <v-card-text>
      <ul>
        <li v-for="(changes, idx) in props.version.changes" :key="idx">
          {{ changes }}
        </li>
      </ul>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import type { Version } from '@/utils/constants'
import { format } from 'date-fns'
import { computed } from 'vue'

type Props = {
  version: Version
}

const props = defineProps<Props>()

const date = computed(() => format(props.version.date, 'yyyy-MM-dd'))
</script>

<style lang="scss">
ul {
  margin-left: 1rem;
}
</style>
