<template>
  <ContentBlock caption="Sessions">
    <ActionRow>
      <v-switch
        label="Show All"
        v-model="showAll"
        color="secondary"
        @update:model-value="handleFilterStateChange"
      ></v-switch>
      <v-spacer></v-spacer>
      <SessionDialog :session="null" :campaign-id="campaignId" @save="handleAdd">
        <template v-slot:default="{ props: activatorProps }">
          <v-btn
            icon="mdi-plus"
            density="compact"
            color="secondary"
            v-bind="activatorProps"
          ></v-btn>
        </template>
      </SessionDialog>
    </ActionRow>
    <v-list-item v-for="session in sessions" :key="session.id">
      <template v-slot:title>
        <FormattedText :value="session.date" format="date"></FormattedText>
      </template>

      <template v-slot:subtitle> {{ session.startTime }} - {{ session.endTime }} </template>

      <template v-slot:prepend>
        <v-icon color="secondary">mdi-calendar</v-icon>
      </template>
    </v-list-item>
  </ContentBlock>
</template>

<script setup lang="ts">
import ActionRow from '@/components/ActionRow.vue'
import SessionDialog from '@/components/calendar/SessionDialog.vue'
import ContentBlock from '@/components/ContentBlock.vue'
import FormattedText from '@/components/FormattedText.vue'
import type { ServerResponse } from '@/models/response'
import type { SessionDTO } from '@/models/session'
import { sessionsConfig } from '@/requests/calendar.request'
import { useCampaignStore } from '@/stores/campaign.store'
import { get, useAPI } from '@/utils/requests'
import { AsyncExecutorBuilder } from '@/utils/snackbars'
import { computed, onMounted, ref } from 'vue'

const campaignStore = useCampaignStore()
const { doRequest } = useAPI()

const sessions = ref<SessionDTO[]>([])
const showAll = ref<boolean>(false)
const campaignId = computed<number>(() => campaignStore.current?.id ?? 0)

const fetchSessions = async () => {
  if (campaignStore.current?.id) {
    const result = await AsyncExecutorBuilder.asyncExecutorBuilder<ServerResponse<SessionDTO[]>>()
      .action(() =>
        doRequest(() =>
          get<SessionDTO[]>(sessionsConfig(campaignStore.current!.id, showAll.value)),
        ),
      )
      .build()
      .execute()
    if (result.result?.success) {
      sessions.value = result.result.data ?? []
    } else {
      sessions.value = []
    }
  }
}

onMounted(async () => {
  await fetchSessions()
})

const handleFilterStateChange = async () => {
  await fetchSessions()
}

const handleAdd = async () => {
  await fetchSessions()
}
</script>
