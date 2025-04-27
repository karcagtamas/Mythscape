<template>
  <v-card>
    <v-card-title class="text-primary">{{ props.caption }}</v-card-title>

    <v-card-text>
      <slot></slot>
    </v-card-text>

    <v-card-actions v-if="$slots.actions">
      <slot name="actions"></slot>
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
type Props = {
  caption: string
}

const props = defineProps<Props>()
</script>
