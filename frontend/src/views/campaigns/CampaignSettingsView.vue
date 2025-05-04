<template>
  <ContentBlock caption="Danger Zone">
    <div class="danger-zone-actions">
      <CampaignDialog mode="edit" @save="handleEdit">
        <template v-slot:default="{ props: activatorProps }">
          <v-btn prepend-icon="mdi-pencil" color="warning" v-bind="activatorProps">Edit</v-btn>
        </template>
      </CampaignDialog>
      <v-btn prepend-icon="mdi-delete" color="error" @click="handleDelete">Delete</v-btn>
      <v-btn prepend-icon="mdi-archive" color="error">Archive</v-btn>
      <v-btn prepend-icon="mdi-restore" color="success">Restore</v-btn>
    </div>
  </ContentBlock>
  <ContentBlock caption="Roles"></ContentBlock>
  <ContentBlock caption="Logs"></ContentBlock>
</template>

<script setup lang="ts">
import ContentBlock from '@/components/ContentBlock.vue'
import CampaignDialog from '@/components/campaigns/CampaignDialog.vue'
import type { ServerResponse } from '@/models/response'
import { campaignDeleteConfig } from '@/requests/campaign.request'
import { useAuthStore } from '@/stores/auth.store'
import { useCampaignStore } from '@/stores/campaign.store'
import { useCommonStore } from '@/stores/common.store'
import { del, useAPI } from '@/utils/requests'
import { AsyncExecutorBuilder } from '@/utils/snackbars'
import { useRouter } from 'vue-router'

const commonStore = useCommonStore()
const { doRequest } = useAPI()
const authStore = useAuthStore()
const campaignStore = useCampaignStore()
const router = useRouter()

const handleEdit = async () => {
  await campaignStore.fetchCampaigns(authStore.userId)
}

const handleDelete = async () => {
  if (campaignStore.current?.id) {
    const campaignId = campaignStore.current.id
    const result = await AsyncExecutorBuilder.asyncExecutorBuilder<ServerResponse<void>>()
      .action(() => doRequest(() => del<void>(campaignDeleteConfig(campaignId))))
      .success('Campaign has been deleted successfully')
      .build()
      .execute()
    if (result.result?.success) {
      commonStore.setMessage(result.message)
      campaignStore.deselect()
      router.push('/app/dashboard')
      campaignStore.fetchCampaigns(authStore.userId)
    }
  }
}
</script>

<style lang="scss">
.danger-zone-actions {
  display: flex;
  flex-direction: row;
  gap: 1rem;
}
</style>
