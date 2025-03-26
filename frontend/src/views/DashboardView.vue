<template>
  <div>Welcome {{ user?.name }}</div>
  <button @click="handleLogout">Logout</button>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth.store'
import type { UserDTO } from '../models/user'
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCommonStore } from '@/stores/common.store'

const authStore = useAuthStore()
const store = useCommonStore()
const router = useRouter()

const user = computed<UserDTO | null>(() => authStore.currentUser)

const handleLogout = () => {
  authStore.logout()
  store.setMessage('Hello')
  router.push('/')
}
</script>
