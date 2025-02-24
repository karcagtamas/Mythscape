<template>
  <div>Welcome {{ user?.fullname }}</div>
  <button @click="handleLogout">Logout</button>
</template>

<script setup lang="ts">
import type { UserDTO } from '../models/user'
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

const store = useStore()
const router = useRouter()

const user = computed<UserDTO | null>(() => store.state.user)

const handleLogout = () => {
  store.dispatch('logout')
  router.push('/')
}
</script>
