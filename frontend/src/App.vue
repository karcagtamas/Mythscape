<template>
  <div class="h-screen w-screen flex flex-col overflow-hidden bg-background text-text antialiased">
    <main class="flex-1 min-h-0 flex flex-col relative">
      <router-view />

      <transition enter-active-class="transform ease-out duration-300 transition"
        enter-from-class="translate-y-2 opacity-0 sm:translate-y-0 sm:translate-x-2"
        enter-to-class="translate-y-0 opacity-100 sm:translate-x-0" leave-active-class="transition ease-in duration-200"
        leave-from-class="opacity-100" leave-to-class="opacity-0">
        <div v-if="show"
          class="fixed top-4 right-4 z-50 flex items-center gap-3 px-4 py-3 rounded-lg shadow-xl border text-sm max-w-sm font-medium backdrop-blur-md"
          :class="alertStyles">
          <span>{{ message?.text }}</span>
        </div>
      </transition>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useCommonStore } from './stores/common.store'

const store = useCommonStore()

const message = computed(() => store.message)
const show = computed(() => store.message !== null)

const alertStyles = computed(() => {
  const type = message.value?.type

  switch (type) {
    case 'error':
      return 'bg-rose-500/10 border-rose-500/20 text-rose-400'
    case 'warning':
      return 'bg-amber-500/10 border-amber-500/20 text-amber-400'
    case 'success':
      return 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400'
    default:
      return 'bg-surface border-border text-text'
  }
})
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
