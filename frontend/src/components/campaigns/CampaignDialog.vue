<template>
  <DialogBase
    :title="title"
    mode="editor"
    :submit-disabled="!valid && !v$.$dirty"
    @submit="handleSubmit"
  >
    <template v-slot:activator="{ props }">
      <slot :props="props"></slot>
    </template>

    <template v-slot:default>
      <v-form v-model="valid">
        <v-text-field
          label="Name"
          type="text"
          density="compact"
          variant="solo"
          v-model="nameField"
          :error-messages="nameFieldErrors"
          @blur="v$.name.$touch()"
        ></v-text-field>
        <v-text-field
          label="Title"
          type="text"
          density="compact"
          variant="solo"
          v-model="titleField"
          :error-messages="titleFieldErrors"
          @blur="v$.title.$touch()"
        ></v-text-field>
        <v-textarea label="Description" density="compact" variant="solo" v-model="descriptionField">
        </v-textarea>
      </v-form>
    </template>
  </DialogBase>
</template>

<script setup lang="ts">
import useVuelidate from '@vuelidate/core'
import DialogBase from '../DialogBase.vue'
import { computed, ref, type Ref } from 'vue'
import { maxLength, required } from '@vuelidate/validators'
import { collectErrors } from '@/utils/validation.helper'
import type { CampaignEditDTO } from '@/models/campaign'
import { AsyncExecutorBuilder } from '@/utils/snackbars'
import type { ServerResponse } from '@/models/response'
import { campaignEditConfig } from '@/requests/campaign.request'
import { post, useAPI } from '@/utils/requests'

type Props = {
  mode: 'create' | 'edit'
}

const props = defineProps<Props>()
const emit = defineEmits({
  save(id: number): boolean {
    return true
  },
})

const title = computed<string>(() =>
  props.mode === 'create' ? 'Create Campaign' : 'Edit Campaign',
)

const { doRequest } = useAPI()

const nameField = ref('')
const titleField = ref('')
const descriptionField = ref('')
const valid = ref(false)

const rules = {
  name: { required, maxLength: maxLength(40) },
  title: { required, maxLength: maxLength(120) },
  description: { required },
}

const v$ = useVuelidate(rules, {
  name: nameField,
  title: titleField,
  description: descriptionField,
})

const nameFieldErrors = computed(() => {
  return collectErrors('Name', v$.value.name.$dirty, [
    { rule: v$.value.name.required, message: '{attr} is required' },
    { rule: v$.value.name.maxLength, message: '{attr} maximum length is 40' },
  ])
})

const titleFieldErrors = computed(() => {
  return collectErrors('Title', v$.value.title.$dirty, [
    { rule: v$.value.title.required, message: '{attr} is required' },
    { rule: v$.value.title.maxLength, message: '{attr} maximum length is 120' },
  ])
})

const handleSubmit = async (isActive: Ref<boolean, boolean>) => {
  v$.value.$validate()

  if (v$.value.$invalid) {
    return
  }

  const dto: CampaignEditDTO = {
    name: nameField.value,
    title: titleField.value,
    description: descriptionField.value,
  }
  const result = await AsyncExecutorBuilder.asyncExecutorBuilder<ServerResponse<number>>()
    .action(() => doRequest(() => post<number, CampaignEditDTO>(campaignEditConfig(), dto)))
    .success('Campaign has been created successfully')
    .build()
    .execute()
  if (result.result?.data) {
    emit('save', result.result.data)
    isActive.value = false
    v$.value.$reset()
    nameField.value = ''
    titleField.value = ''
    descriptionField.value = ''
  }
}
</script>
