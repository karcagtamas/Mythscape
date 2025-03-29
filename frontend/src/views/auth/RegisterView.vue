<template>
  <v-card elevation="4" class="block">
    <v-card-title class="text-h5 text-primary font-weight-bold">Registration</v-card-title>
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
          label="E-mail address"
          type="text"
          density="compact"
          variant="solo"
          v-model="email"
          prepend-inner-icon="mdi-email"
          :error-messages="emailErrors"
          @blur="v$.email.$touch()"
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
        <v-text-field
          label="Password Confirm"
          :type="visisblePasswordConfirm ? 'text' : 'password'"
          density="compact"
          variant="solo"
          v-model="passwordConfirm"
          :append-inner-icon="visisblePasswordConfirm ? 'mdi-eye-off' : 'mdi-eye'"
          prepend-inner-icon="mdi-lock-outline"
          @click:append-inner="visisblePasswordConfirm = !visisblePasswordConfirm"
          :error-messages="passwordConfirmErrors"
          @blur="v$.passwordConfirm.$touch()"
        ></v-text-field>
        <v-text-field
          label="Full Name"
          type="text"
          density="compact"
          variant="solo"
          v-model="fullname"
          prepend-inner-icon="mdi-account"
          :error-messages="fullnameErrors"
          @blur="v$.fullname.$touch()"
        ></v-text-field>
        <v-btn elevation="2" raised block color="primary" @click="handleSubmit" :disabled="!valid"
          >Register</v-btn
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
            >To Login</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import type { RegisterDTO } from '../../models/auth'
import useVuelidate from '@vuelidate/core'
import { maxLength, minLength, required, email as email$, sameAs } from '@vuelidate/validators'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { collectErrors } from '@/utils/validation.helper'
import { post, useAPI } from '@/utils/requests'
import { registerConfig } from '@/requests/auth.request'
import { useCommonStore } from '@/stores/common.store'
import { AsyncExecutorBuilder } from '@/utils/snackbars'
import type { ServerResponse } from '@/models/response'

const router = useRouter()
const commonStore = useCommonStore()
const { doRequest } = useAPI()

const username = ref('')
const email = ref('')
const password = ref('')
const passwordConfirm = ref('')
const fullname = ref('')
const valid = ref(false)
const visisblePassword = ref(false)
const visisblePasswordConfirm = ref(false)

const rules = {
  username: { required, minLength: minLength(6), maxLength: maxLength(24) },
  email: { required, email$, maxLength: maxLength(120) },
  password: { required, minLength: minLength(8), maxLength: maxLength(32) },
  passwordConfirm: { sameAs: sameAs(password) },
  fullname: { required, maxLength: maxLength(80) },
}

const v$ = useVuelidate(rules, { username, email, password, passwordConfirm, fullname })

const usernameErrors = computed(() => {
  return collectErrors('User Name', v$.value.username.$dirty, [
    { rule: v$.value.username.required, message: '{attr} is required' },
    { rule: v$.value.username.minLength, message: '{attr} minimum length is 6' },
    { rule: v$.value.username.maxLength, message: '{attr} maximum length is 24' },
  ])
})

const emailErrors = computed(() => {
  return collectErrors('E-mail address', v$.value.email.$dirty, [
    { rule: v$.value.email.required, message: '{attr} is required' },
    { rule: v$.value.email.email, message: '{attr} must be e-mail formatted' },
    { rule: v$.value.email.maxLength, message: '{attr} maximum length is 120' },
  ])
})

const passwordErrors = computed(() => {
  return collectErrors('Password', v$.value.password.$dirty, [
    { rule: v$.value.password.required, message: '{attr} is required' },
    { rule: v$.value.password.minLength, message: '{attr} minimum length is 8' },
    { rule: v$.value.password.maxLength, message: '{attr} maximum length is 32' },
  ])
})

const passwordConfirmErrors = computed(() => {
  return collectErrors('Password Confirm', v$.value.passwordConfirm.$dirty, [
    { rule: v$.value.passwordConfirm.sameAs, message: '{attr} must be same as Password' },
  ])
})

const fullnameErrors = computed(() => {
  return collectErrors('Full Name', v$.value.fullname.$dirty, [
    { rule: v$.value.fullname.required, message: '{attr} is required' },
    { rule: v$.value.fullname.maxLength, message: '{attr} maximum length is 80' },
  ])
})

const handleSubmit = async () => {
  v$.value.$validate()

  if (v$.value.$invalid) {
    return
  }

  const dto: RegisterDTO = {
    username: username.value,
    email: email.value,
    password: password.value,
    passwordConfirm: passwordConfirm.value,
    fullname: fullname.value,
  }

  const result = await AsyncExecutorBuilder.asyncExecutorBuilder<ServerResponse<number>>()
    .action(() => doRequest(() => post<number, RegisterDTO>(registerConfig(), dto)))
    .success('The registration was successful')
    .build()
    .execute()
  commonStore.setMessage(result.message)
  router.push('/auth/login')
}
</script>

<style lang="scss" scoped>
.block {
  width: min(50%, 400px);
}
</style>
