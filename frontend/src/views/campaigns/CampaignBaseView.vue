<template>
  <v-navigation-drawer v-if="current !== null" width="244">
    <v-sheet color="grey-lighten-5 text-center py-3" height="128" width="100%">
      <img class="main-color" src="/main.svg" height="100%" />
    </v-sheet>

    <v-list
      :lines="false"
      density="compact"
      nav
      :selected="selectedPage"
      :items="pages"
      :item-value="(item) => item.key"
      color="primary"
      selectable
      @update:selected="handlePageSelect"
    >
      <template v-slot:prepend="{ item }">
        <v-icon>{{ item.icon }}</v-icon>
      </template>
    </v-list>
  </v-navigation-drawer>

  <div class="page-frame">
    <v-app-bar color="primary" v-if="current !== null" rounded density="compact">
      <v-app-bar-title> {{ current.title }} ({{ current.name }}) </v-app-bar-title>
    </v-app-bar>

    <router-view v-if="current"></router-view>
  </div>
</template>

<script setup lang="ts">
import type { CampaignDTO } from '@/models/campaign'
import { useCampaignStore } from '@/stores/campaign.store'
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

type Page = { key: string; title: string; icon: string; path: string }

const pages: Page[] = [
  { key: 'data', title: 'Data', icon: 'mdi-home-circle', path: '' },
  { key: 'settings', title: 'Settings', icon: 'mdi-cog', path: '/settings' },
  { key: 'notes', title: 'Notes', icon: 'mdi-note-multiple', path: '/notes' },
  { key: 'sessions', title: 'Sessions', icon: 'mdi-calendar-multiple', path: '/sessions' },
]

const campaignStore = useCampaignStore()
const router = useRouter()
const route = useRoute()
const id = route.params.campaignId

onMounted(async () => {
  await campaignStore.selectById(+id, route.meta.page as string)
})

const current = computed<CampaignDTO | null>(() => campaignStore.current)
const selectedPage = computed<string[]>(() => [campaignStore.page])

const handlePageSelect = (selection: string[]) => {
  const page = selection.map((key) => pages.find((p) => p.key === key))[0]

  if (page) {
    campaignStore.setPage(page.key)
    router.push(`/app/campaigns/${current.value?.id}${page.path}`)
  }
}
</script>

<style lang="scss">
.page-frame {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 0.8rem;
  gap: 1rem;
  overflow: hidden;
}
</style>
