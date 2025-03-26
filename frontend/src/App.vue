<template>
  <v-app>
    <v-main>
      <v-snackbar v-model="message.show" :timeout="timeout"> {{ message.text }} </v-snackbar>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useCommonStore } from './stores/common.store'

const timeout = ref(2000)

const store = useCommonStore()

const message = computed(() => ({ text: store.message, show: !!store.message }))
</script>
