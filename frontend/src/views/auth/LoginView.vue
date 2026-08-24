<template>
  <div class="w-full max-w-[400px] p-6 rounded-xl bg-surface border border-border shadow-2xl backdrop-blur">
    <h2 class="text-xl font-bold tracking-wider text-primary uppercase mb-6">Login</h2>

    <form @submit="onSubmit" class="space-y-4 font-sans text-sm">
      <!-- Username field -->
      <div>
        <div class="relative flex items-center">
          <User class="absolute left-3 w-4 h-4 text-slate-500" />
          <input v-model="username" v-bind="usernameAttrs" type="text" placeholder="User Name"
            class="w-full pl-10 pr-4 py-2 rounded bg-background border border-border focus:border-primary focus:outline-none transition text-slate-200"
            :class="{ 'border-rose-500/50 focus:border-rose-500': errors.username }" />
        </div>
        <p v-if="errors.username" class="text-[11px] font-mono text-rose-400 mt-1 pl-1">{{ errors.username }}</p>
      </div>

      <!-- Password field -->
      <div>
        <div class="relative flex items-center">
          <Lock class="absolute left-3 w-4 h-4 text-slate-500" />
          <input v-model="password" v-bind="passwordAttrs" :type="visiblePassword ? 'text' : 'password'"
            placeholder="Password"
            class="w-full pl-10 pr-10 py-2 rounded bg-background border border-border focus:border-primary focus:outline-none transition text-slate-200"
            :class="{ 'border-rose-500/50 focus:border-rose-500': errors.password }" />
          <button type="button" @click="visiblePassword = !visiblePassword"
            class="absolute right-3 text-slate-500 hover:text-slate-300 transition">
            <EyeOff v-if="visiblePassword" class="w-4 h-4" />
            <Eye v-else class="w-4 h-4" />
          </button>
        </div>
        <p v-if="errors.password" class="text-[11px] font-mono text-rose-400 mt-1 pl-1">{{ errors.password }}</p>
      </div>

      <!-- Submit -->
      <button type="submit" :disabled="!meta.valid || isSubmitting"
        class="w-full py-2 rounded bg-primary text-white font-medium hover:bg-primary/90 transition text-sm uppercase tracking-wide disabled:opacity-40 disabled:cursor-not-allowed shadow-md shadow-primary/10 mt-2">
        <span v-if="isSubmitting">Authenticating...</span>
        <span v-else>Login</span>
      </button>
    </form>

    <!-- Navigation Footer -->
    <div class="mt-6 pt-4 border-t border-border flex flex-col gap-2 bg-background/30 rounded p-3">
      <p class="text-xs text-slate-400">If you do not have an account yet:</p>
      <button @click="router.push('/auth/register')"
        class="text-xs text-secondary hover:underline self-end flex items-center gap-0.5">
        To Register <span>➔</span>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth.store'
import type { LoginDTO, TokenDTO } from '../../models/auth'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCommonStore } from '@/stores/common.store'
import type { ServerResponse } from '@/models/response'
import { toTypedSchema } from '@vee-validate/zod'
import * as zod from 'zod'
import { useApi } from '@/plugins/api'
import { useForm } from 'vee-validate'

const router = useRouter()
const authStore = useAuthStore()
const commonStore = useCommonStore()

const visiblePassword = ref(false)

const loginSchema = toTypedSchema(
  zod.object({
    username: zod.string().min(1, 'User Name is required'),
    password: zod.string().min(1, 'Password is required'),
  })
)

const { errors, defineField, handleSubmit, meta, isSubmitting } = useForm({
  validationSchema: loginSchema,
})

const [username, usernameAttrs] = defineField('username', { validateOnBlur: true })
const [password, passwordAttrs] = defineField('password', { validateOnBlur: true })

const onSubmit = handleSubmit(async (values) => {
  const dto: LoginDTO = {
    username: values.username,
    password: values.password,
  }

  // Execute type-safe POST via useApi
  const { data, error } = await useApi('/auth/login')
    .post(dto)
    .json<ServerResponse<TokenDTO>>()

  if (!error.value && data.value?.data) {
    // Populate your updated, Axios-free auth store pipelines
    authStore.login(data.value.data)
    await authStore.fetchUser()

    commonStore.setMessage({ text: 'You successfully logged in', type: 'success' })
    router.push('/app/dashboard')
  } else {
    commonStore.setMessage({ text: 'Invalid credentials or connection dropped.', type: 'error' })
  }
})
</script>