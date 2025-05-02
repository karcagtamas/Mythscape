<template>
  <ContentBlock caption="Description"> {{ campaign?.description }} </ContentBlock>
  <ContentBlock caption="Tags">
    <div class="tags">
      <ColorTag
        v-for="tag in tags"
        :key="tag.id"
        :caption="tag.caption"
        :color="tag.color"
        closable
        @close="handleTagDelete(tag.id)"
      ></ColorTag>
    </div>

    <template v-slot:actions>
      <CampaignTagDialog
        v-if="campaign?.id"
        mode="create"
        :campaign-id="campaign.id"
        @save="handleTagAdd"
      >
        <template v-slot:default="{ props: activatorProps }">
          <v-btn
            prepend-icon="mdi-plus"
            variant="outlined"
            color="secondary"
            v-bind="activatorProps"
            >Append</v-btn
          >
        </template>
      </CampaignTagDialog>
    </template>
  </ContentBlock>
  <ContentBlock caption="Members">
    <v-list>
      <v-list-item :title="campaign?.creator.name" subtitle="Owner">
        <template v-slot:prepend>
          <TextAvatar color="primary" :value="campaign?.creator.name"></TextAvatar>
        </template>
      </v-list-item>
      <v-divider inset></v-divider>
      <v-list-item v-for="member in members" :key="member.id" :title="member.name">
        <template v-slot:prepend>
          <TextAvatar color="secondary" :value="member.name"></TextAvatar>
        </template>
      </v-list-item>
    </v-list>
  </ContentBlock>
  <ContentBlock caption="Meta Data">
    <DataRow caption="Creator" :value="campaign?.creator.name"></DataRow>
    <DateDataRow caption="Creation" :value="campaign?.creation" format="date-time"></DateDataRow>
    <DateDataRow
      caption="Last Update"
      :value="campaign?.lastUpdate"
      format="date-time"
    ></DateDataRow>
  </ContentBlock>
</template>

<script setup lang="ts">
import ContentBlock from '@/components/ContentBlock.vue'
import DataRow from '@/components/DataRow.vue'
import DateDataRow from '@/components/DateDataRow.vue'
import ColorTag from '@/components/ColorTag.vue'
import TextAvatar from '@/components/TextAvatar.vue'
import CampaignTagDialog from '@/components/campaigns/CampaignTagDialog.vue'
import { useCampaignStore } from '@/stores/campaign.store'
import { computed } from 'vue'
import type { CampaignDTO, CampaignMemberDTO, CampaignTagDTO } from '@/models/campaign'
import { AsyncExecutorBuilder } from '@/utils/snackbars'
import type { ServerResponse } from '@/models/response'
import { del, useAPI } from '@/utils/requests'
import { useCommonStore } from '@/stores/common.store'
import { campaignTagDeleteConfig } from '@/requests/campaign.request'

const campaignStore = useCampaignStore()
const { doRequest } = useAPI()
const commonStore = useCommonStore()

const campaign = computed<CampaignDTO | null>(() => campaignStore.current)
const tags = computed<CampaignTagDTO[]>(() => campaignStore.tags)
const members = computed<CampaignMemberDTO[]>(() => campaignStore.members)

const handleTagAdd = () => {
  if (campaign.value?.id) {
    campaignStore.fetchTags(campaign.value.id)
  }
}

const handleTagDelete = async (tagId: number) => {
  if (campaign.value?.id) {
    const campaignId = campaign.value.id
    const result = await AsyncExecutorBuilder.asyncExecutorBuilder<ServerResponse<void>>()
      .action(() => doRequest(() => del<void>(campaignTagDeleteConfig(campaignId, tagId))))
      .success('Tag has been created successfully')
      .build()
      .execute()
    if (result.result?.data) {
      commonStore.setMessage(result.message)
      campaignStore.fetchTags(campaignId)
    }
  }
}
</script>

<style lang="scss">
.tags {
  display: flex;
  flex-direction: row;
  gap: 0.4rem;
}
</style>
