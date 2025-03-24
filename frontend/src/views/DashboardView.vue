<template>
  <div>Welcome {{ user?.fullname }}</div>
  <button @click="handleLogout">Logout</button>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth.store'
import type { UserDTO } from '../models/user'
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const user = computed<UserDTO | null>(() => authStore.user)

const handleLogout = () => {
  authStore.logout()
  router.push('/')
}
</script>
