<template>
  <ContentBlock caption="Danger Zone">
    <div class="danger-zone-actions">
      <v-btn prepend-icon="mdi-pencil" color="warning">Edit</v-btn>
      <v-btn prepend-icon="mdi-delete" color="error">Delete</v-btn>
      <v-btn prepend-icon="mdi-archive" color="error">Archive</v-btn>
      <v-btn prepend-icon="mdi-restore" color="success">Restore</v-btn>
    </div>
  </ContentBlock>
  <ContentBlock caption="Roles"></ContentBlock>
  <ContentBlock caption="Logs"></ContentBlock>
</template>

<script setup lang="ts">
import ContentBlock from '@/components/ContentBlock.vue'
</script>

<style lang="scss">
.danger-zone-actions {
  display: flex;
  flex-direction: row;
  gap: 1rem;
}
</style>
