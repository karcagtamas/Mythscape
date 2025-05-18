<template>
  <v-app class="application">
    <v-main class="main">
      <v-snackbar
        :model-value="show"
        :timeout="timeout"
        :color="message?.type"
        location="top right"
      >
        {{ message?.text }}
      </v-snackbar>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useCommonStore } from './stores/common.store'
const store = useCommonStore()

const timeout = ref(1000)

const message = computed(() => store.message)
const show = computed(() => store.message !== null)
</script>

<style scoped lang="scss">
.application {
  max-height: 100vh;
}

.main {
  overflow: hidden;
  display: flex;
  flex-direction: column;
  flex: 1;
}
</style>
