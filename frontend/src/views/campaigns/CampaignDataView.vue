<template>
  <ContentBlock caption="Description"> {{ campaign?.description }} </ContentBlock>
  <ContentBlock caption="Tags">
    <ColorTag
      v-for="tag in tags"
      :key="tag.id"
      :caption="tag.caption"
      :color="tag.color"
    ></ColorTag>

    <template v-slot:actions>
      <v-btn prepend-icon="mdi-plus" variant="outlined" color="secondary">Append</v-btn>
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
