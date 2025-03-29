<template>
  <v-app>
    <v-main>
      <v-snackbar v-model="show" :timeout="timeout" :color="message?.type" location="top right">
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

const timeout = ref(100)

const message = computed(() => store.message)
const show = computed(() => store.message !== null)
</script>
