<template>
  <DialogBase
    :title="title"
    mode="editor"
    :submit-disabled="!valid || !v$.$anyDirty"
    @submit="handleSubmit"
    @cancel="reset"
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
import { computed, onMounted, ref, type Ref } from 'vue'
import { maxLength, required } from '@vuelidate/validators'
import { collectErrors } from '@/utils/validation.helper'
import type { CampaignDTO, CampaignEditDTO } from '@/models/campaign'
import { AsyncExecutorBuilder } from '@/utils/snackbars'
import type { ServerResponse } from '@/models/response'
import { campaignCreateConfig, campaignEditConfig } from '@/requests/campaign.request'
import { post, put, useAPI } from '@/utils/requests'
import { useCommonStore } from '@/stores/common.store'

type Props = {
  campaign: CampaignDTO | null
}

const props = defineProps<Props>()
const emit = defineEmits({
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  save(id: number | null): boolean {
    return true
  },
})

const { doRequest } = useAPI()
const commonStore = useCommonStore()

const nameField = ref('')
const titleField = ref('')
const descriptionField = ref('')
const valid = ref(false)

const isEdit = computed<boolean>(() => props.campaign !== null)

const title = computed<string>(() => (!isEdit.value ? 'Create Campaign' : 'Edit Campaign'))

onMounted(async () => {
  if (isEdit.value && props.campaign) {
    nameField.value = props.campaign.name
    titleField.value = props.campaign.title
    descriptionField.value = props.campaign.description ?? ''
  }
})

const rules = {
  name: { required, maxLength: maxLength(40) },
  title: { required, maxLength: maxLength(120) },
  description: {},
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

const reset = () => {
  v$.value.$reset()

  if (!isEdit.value) {
    nameField.value = ''
    titleField.value = ''
    descriptionField.value = ''
  }
}

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
  const result = !isEdit.value
    ? await AsyncExecutorBuilder.asyncExecutorBuilder<ServerResponse<number>>()
        .action(() => doRequest(() => post<number, CampaignEditDTO>(campaignCreateConfig(), dto)))
        .success('Campaign has been created successfully')
        .build()
        .execute()
    : await AsyncExecutorBuilder.asyncExecutorBuilder<ServerResponse<void>>()
        .action(() =>
          doRequest(() => put<void, CampaignEditDTO>(campaignEditConfig(props.campaign!.id), dto)),
        )
        .success('Campaign has been edited successfully')
        .build()
        .execute()
  if (result.result?.success) {
    emit('save', result.result.data ?? null)
    isActive.value = false
    commonStore.setMessage(result.message)
    reset()
  }
}
</script>
