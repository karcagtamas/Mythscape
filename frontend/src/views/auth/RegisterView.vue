<template>
  <div class="w-full max-w-[400px] p-6 rounded-xl bg-surface border border-border shadow-2xl backdrop-blur">
    <h2 class="text-xl font-bold tracking-wider text-primary uppercase mb-6">Registration</h2>

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

      <!-- Email field -->
      <div>
        <div class="relative flex items-center">
          <Mail class="absolute left-3 w-4 h-4 text-slate-500" />
          <input v-model="email" v-bind="emailAttrs" type="email" placeholder="E-mail address"
            class="w-full pl-10 pr-4 py-2 rounded bg-background border border-border focus:border-primary focus:outline-none transition text-slate-200"
            :class="{ 'border-rose-500/50 focus:border-rose-500': errors.email }" />
        </div>
        <p v-if="errors.email" class="text-[11px] font-mono text-rose-400 mt-1 pl-1">{{ errors.email }}</p>
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

      <!-- Password confirm field -->
      <div>
        <div class="relative flex items-center">
          <Lock class="absolute left-3 w-4 h-4 text-slate-500" />
          <input v-model="passwordConfirm" v-bind="passwordConfirmAttrs"
            :type="visiblePasswordConfirm ? 'text' : 'password'" placeholder="Password Confirm"
            class="w-full pl-10 pr-10 py-2 rounded bg-background border border-border focus:border-primary focus:outline-none transition text-slate-200"
            :class="{ 'border-rose-500/50 focus:border-rose-500': errors.passwordConfirm }" />
          <button type="button" @click="visiblePasswordConfirm = !visiblePasswordConfirm"
            class="absolute right-3 text-slate-500 hover:text-slate-300 transition">
            <EyeOff v-if="visiblePasswordConfirm" class="w-4 h-4" />
            <Eye v-else class="w-4 h-4" />
          </button>
        </div>
        <p v-if="errors.passwordConfirm" class="text-[11px] font-mono text-rose-400 mt-1 pl-1">{{ errors.passwordConfirm
        }}</p>
      </div>

      <!-- Full name field -->
      <div>
        <div class="relative flex items-center">
          <UserCheck class="absolute left-3 w-4 h-4 text-slate-500" />
          <input v-model="fullname" v-bind="fullnameAttrs" type="text" placeholder="Full Name"
            class="w-full pl-10 pr-4 py-2 rounded bg-background border border-border focus:border-primary focus:outline-none transition text-slate-200"
            :class="{ 'border-rose-500/50 focus:border-rose-500': errors.fullname }" />
        </div>
        <p v-if="errors.fullname" class="text-[11px] font-mono text-rose-400 mt-1 pl-1">{{ errors.fullname }}</p>
      </div>

      <button type="submit" :disabled="!meta.valid || isSubmitting"
        class="w-full py-2 rounded bg-primary text-white font-medium hover:bg-primary/90 transition text-sm uppercase tracking-wide disabled:opacity-40 disabled:cursor-not-allowed shadow-md shadow-primary/10 mt-2">
        <span v-if="isSubmitting">Creating Account...</span>
        <span v-else>Register</span>
      </button>
    </form>
    <div class="mt-6 pt-4 border-t border-border flex flex-col gap-2 bg-background/30 rounded p-3">
      <p class="text-xs text-slate-400">Already mapped an account?</p>
      <button @click="router.push('/auth/login')"
        class="text-xs text-secondary hover:underline self-end flex items-center gap-0.5">
        To Login <span>➔</span>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { RegisterDTO } from '../../models/auth'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCommonStore } from '@/stores/common.store'
import { User, Mail, Lock, Eye, EyeOff, UserCheck } from '@lucide/vue'
import * as zod from 'zod'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
import { useApi } from '@/plugins/api'

const router = useRouter()
const commonStore = useCommonStore()

const visiblePassword = ref(false)
const visiblePasswordConfirm = ref(false)

const registerSchema = toTypedSchema(
  zod.object({
    username: zod.string().min(1, 'User Name is required').min(6, 'Minimum length is 6').max(24, 'Maximum length is 24'),
    email: zod.string().min(1, 'E-mail address is required').email('Must be e-mail formatted').max(120, 'Maximum length is 120'),
    password: zod.string().min(1, 'Password is required').min(8, 'Minimum length is 8').max(32, 'Maximum length is 32'),
    passwordConfirm: zod.string().min(1, 'Password confirmation is required'),
    fullname: zod.string().min(1, 'Full Name is required').max(80, 'Maximum length is 80'),
  }).refine((data) => data.password === data.passwordConfirm, {
    message: 'Password Confirm must be same as Password',
    path: ['passwordConfirm'],
  })
)

const { errors, defineField, handleSubmit, meta, isSubmitting } = useForm({
  validationSchema: registerSchema,
})

const [username, usernameAttrs] = defineField('username', { validateOnBlur: true })
const [email, emailAttrs] = defineField('email', { validateOnBlur: true })
const [password, passwordAttrs] = defineField('password', { validateOnBlur: true })
const [passwordConfirm, passwordConfirmAttrs] = defineField('passwordConfirm', { validateOnBlur: true })
const [fullname, fullnameAttrs] = defineField('fullname', { validateOnBlur: true })

const onSubmit = handleSubmit(async (values) => {
  const dto: RegisterDTO = {
    username: values.username,
    email: values.email,
    password: values.password,
    passwordConfirm: values.passwordConfirm,
    fullname: values.fullname,
  }

  const { error } = await useApi('/auth/register')
    .post(dto)
    .json()

  if (!error.value) {
    commonStore.setMessage({ text: 'The registration was successful', type: 'success' })
    router.push('/auth/login')
  } else {
    commonStore.setMessage({ text: 'Registration processing failed. Please try again.', type: 'error' })
  }
})
</script>
