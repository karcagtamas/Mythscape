<template>
  <v-card elevation="4" class="block">
    <v-card-title class="text-h5 text-primary font-weight-bold">Login</v-card-title>
    <v-card-text>
      <v-form v-model="valid">
        <v-text-field
          label="User Name"
          type="text"
          density="compact"
          variant="solo"
          v-model="username"
          prepend-inner-icon="mdi-account"
          :error-messages="usernameErrors"
          @blur="v$.username.$touch()"
        ></v-text-field>
        <v-text-field
          label="Password"
          :type="visisblePassword ? 'text' : 'password'"
          density="compact"
          variant="solo"
          v-model="password"
          :append-inner-icon="visisblePassword ? 'mdi-eye-off' : 'mdi-eye'"
          prepend-inner-icon="mdi-lock-outline"
          @click:append-inner="visisblePassword = !visisblePassword"
          :error-messages="passwordErrors"
          @blur="v$.password.$touch()"
        ></v-text-field>
        <v-btn elevation="2" raised block color="primary" @click="handleSubmit" :disabled="!valid"
          >Login</v-btn
        >
      </v-form>
    </v-card-text>
    <v-divider></v-divider>
    <v-card-text>
      <v-card variant="tonal">
        <v-card-title></v-card-title>
        <v-card-subtitle> If you does not have any account yet:</v-card-subtitle>
        <v-card-actions class="justify-end">
          <v-btn
            variant="text"
            color="secondary"
            append-icon="mdi-chevron-right"
            @click="$router.push({ path: '/auth/register' })"
            >To Register</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth.store'
import type { LoginDTO, TokenDTO } from '../../models/auth'
import useVuelidate from '@vuelidate/core'
import { required } from '@vuelidate/validators'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCommonStore } from '@/stores/common.store'
import { AsyncExecutorBuilder } from '@/utils/snackbars'
import { post, useAPI } from '@/utils/requests'
import { loginConfig } from '@/requests/auth.request'
import type { ServerResponse } from '@/models/response'

const commonStore = useCommonStore()
const authStore = useAuthStore()
const { doRequest } = useAPI()
const router = useRouter()

const username = ref('')
const password = ref('')
const valid = ref(false)
const visisblePassword = ref(false)

const rules = {
  username: { required },
  password: { required },
}

const v$ = useVuelidate(rules, { username, password })

const usernameErrors = computed(() => {
  if (!v$.value.username.$dirty) {
    return []
  }

  return v$.value.username.required.$invalid ? ['User Name is required'] : []
})

const passwordErrors = computed(() => {
  if (!v$.value.password.$dirty) {
    return []
  }

  return v$.value.password.required.$invalid ? ['Password is required'] : []
})

const handleSubmit = async () => {
  v$.value.$validate()

  if (v$.value.$invalid) {
    return
  }

  const dto: LoginDTO = { username: username.value, password: password.value }
  const result = await AsyncExecutorBuilder.asyncExecutorBuilder<ServerResponse<TokenDTO>>()
    .action(() => doRequest(() => post<TokenDTO, LoginDTO>(loginConfig(), dto)))
    .success('You successfully logged in')
    .build()
    .execute()
  if (result.result?.data) {
    authStore.login(result.result?.data)
    await authStore.fetchUser()
    commonStore.setMessage(result.message)
    router.push('/app/dashboard')
  }
}
</script>

<style lang="scss" scoped>
.block {
  width: min(50%, 400px);
}
</style>
