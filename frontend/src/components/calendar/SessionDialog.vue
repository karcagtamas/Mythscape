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
          label="Date"
          type="date"
          density="compact"
          variant="solo"
          v-model="dateField"
          :error-messages="dateFieldErrors"
          @blur="v$.date.$touch()"
        ></v-text-field>
        <v-text-field
          label="Start Time"
          type="time"
          density="compact"
          variant="solo"
          v-model="startTimeField"
          :error-messages="startTimeFieldErrors"
          @blur="v$.startTime.$touch()"
        ></v-text-field>
        <v-text-field
          label="End Time"
          type="time"
          density="compact"
          variant="solo"
          v-model="endTimeField"
          :error-messages="endTimeFieldErrors"
          @blur="v$.endTime.$touch()"
        ></v-text-field>
      </v-form>
    </template>
  </DialogBase>
</template>

<script setup lang="ts">
import useVuelidate from '@vuelidate/core'
import DialogBase from '../DialogBase.vue'
import { computed, onMounted, ref, type Ref } from 'vue'
import { required } from '@vuelidate/validators'
import { collectErrors } from '@/utils/validation.helper'
import { AsyncExecutorBuilder } from '@/utils/snackbars'
import type { ServerResponse } from '@/models/response'
import { post, put, useAPI } from '@/utils/requests'
import { useCommonStore } from '@/stores/common.store'
import type { SessionDTO, SessionEditDTO } from '@/models/session'
import { sessionCreateConfig, sessionEditConfig } from '@/requests/calendar.request'

type Props = {
  session: SessionDTO | null
  campaignId: number
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

const dateField = ref<Date>(new Date())
const startTimeField = ref('')
const endTimeField = ref('')
const valid = ref(false)

const isEdit = computed<boolean>(() => props.session !== null)

const title = computed<string>(() => (!isEdit.value ? 'Create Session' : 'Edit Session'))

onMounted(async () => {
  if (isEdit.value && props.session) {
    dateField.value = props.session.date
    startTimeField.value = props.session.startTime
    endTimeField.value = props.session.endTime
  }
})

const rules = {
  date: { required },
  startTime: { required },
  endTime: { required },
}

const v$ = useVuelidate(rules, {
  date: dateField,
  startTime: startTimeField,
  endTime: endTimeField,
})

const dateFieldErrors = computed(() => {
  return collectErrors('Date', v$.value.date.$dirty, [
    { rule: v$.value.date.required, message: '{attr} is required' },
  ])
})

const startTimeFieldErrors = computed(() => {
  return collectErrors('Title', v$.value.startTime.$dirty, [
    { rule: v$.value.startTime.required, message: '{attr} is required' },
  ])
})

const endTimeFieldErrors = computed(() => {
  return collectErrors('Title', v$.value.endTime.$dirty, [
    { rule: v$.value.endTime.required, message: '{attr} is required' },
  ])
})

const reset = () => {
  v$.value.$reset()

  if (!isEdit.value) {
    dateField.value = new Date()
    startTimeField.value = ''
    endTimeField.value = ''
  }
}

const handleSubmit = async (isActive: Ref<boolean, boolean>) => {
  v$.value.$validate()

  if (v$.value.$invalid) {
    return
  }

  const dto: SessionEditDTO = {
    date: dateField.value,
    startTime: startTimeField.value,
    endTime: endTimeField.value,
    campaignId: props.campaignId,
  }
  const result = !isEdit.value
    ? await AsyncExecutorBuilder.asyncExecutorBuilder<ServerResponse<number>>()
        .action(() => doRequest(() => post<number, SessionEditDTO>(sessionCreateConfig(), dto)))
        .success('Session has been created successfully')
        .build()
        .execute()
    : await AsyncExecutorBuilder.asyncExecutorBuilder<ServerResponse<void>>()
        .action(() =>
          doRequest(() => put<void, SessionEditDTO>(sessionEditConfig(props.session!.id), dto)),
        )
        .success('Session has been edited successfully')
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
