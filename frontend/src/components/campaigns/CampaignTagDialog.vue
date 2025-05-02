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
          v-model="captionField"
          :error-messages="captionFieldErrors"
          @blur="v$.caption.$touch()"
        ></v-text-field>
        <v-text-field
          label="Color"
          type="text"
          density="compact"
          variant="solo"
          v-model="colorField"
          :error-messages="colorFieldErrors"
          @blur="v$.color.$touch()"
        ></v-text-field>
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
import type { CampaignTagEditDTO } from '@/models/campaign'
import { AsyncExecutorBuilder } from '@/utils/snackbars'
import type { ServerResponse } from '@/models/response'
import { campaignTagCreateConfig } from '@/requests/campaign.request'
import { post, useAPI } from '@/utils/requests'
import { useCommonStore } from '@/stores/common.store'

type Props = {
  mode: 'create' | 'edit'
  campaignId: number
}

const props = defineProps<Props>()
const emit = defineEmits({
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  save(id: number): boolean {
    return true
  },
})

const title = computed<string>(() => (props.mode === 'create' ? 'Create Tag' : 'Edit Tag'))

const { doRequest } = useAPI()
const commonStore = useCommonStore()

const captionField = ref('')
const colorField = ref('')
const valid = ref(false)

const rules = {
  caption: { required, maxLength: maxLength(40) },
  color: { required },
}

const v$ = useVuelidate(rules, {
  caption: captionField,
  color: colorField,
})

const captionFieldErrors = computed(() => {
  return collectErrors('Caption', v$.value.caption.$dirty, [
    { rule: v$.value.caption.required, message: '{attr} is required' },
    { rule: v$.value.caption.maxLength, message: '{attr} maximum length is 40' },
  ])
})

const colorFieldErrors = computed(() => {
  return collectErrors('Color', v$.value.color.$dirty, [
    { rule: v$.value.color.required, message: '{attr} is required' },
  ])
})

const reset = () => {
  v$.value.$reset()
  captionField.value = ''
  colorField.value = ''
}

const handleSubmit = async (isActive: Ref<boolean, boolean>) => {
  v$.value.$validate()

  if (v$.value.$invalid) {
    return
  }

  const dto: CampaignTagEditDTO = {
    caption: captionField.value,
    color: colorField.value,
  }

  const result = await AsyncExecutorBuilder.asyncExecutorBuilder<ServerResponse<number>>()
    .action(() =>
      doRequest(() =>
        post<number, CampaignTagEditDTO>(campaignTagCreateConfig(props.campaignId), dto),
      ),
    )
    .success('Tag has been created successfully')
    .build()
    .execute()
  if (result.result?.data) {
    emit('save', result.result.data)
    isActive.value = false
    commonStore.setMessage(result.message)
    reset()
  }
}
</script>
