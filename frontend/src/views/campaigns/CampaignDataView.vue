<template>
  <ContentBlock caption="Description"> {{ campaign?.description }} </ContentBlock>
  <ContentBlock caption="Tags"> </ContentBlock>
  <ContentBlock caption="Members"> </ContentBlock>
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
import { useCampaignStore } from '@/stores/campaign.store'
import { computed, onMounted } from 'vue'

const campaignStore = useCampaignStore()

onMounted(async () => {
  await campaignStore.fetchTags(campaignStore.current?.id ?? 0)
  await campaignStore.fetchMembers(campaignStore.current?.id ?? 0)
})

const campaign = computed(() => campaignStore.current)
const tags = computed(() => campaignStore.tags)
const members = computed(() => campaignStore.members)
</script>
