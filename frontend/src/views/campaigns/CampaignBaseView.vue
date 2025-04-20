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

  <v-app-bar v-if="current !== null"></v-app-bar>

  <router-view></router-view>
</template>

<script setup lang="ts">
import type { CampaignDTO } from '@/models/campaign'
import { useCampaignStore } from '@/stores/campaign.store'
import { computed } from 'vue'
import { useRouter } from 'vue-router'

type Page = { key: string; title: string; icon: string; path: string }

const pages: Page[] = [
  { key: 'data', title: 'Data', icon: 'mdi-home-circle', path: '' },
  { key: 'settings', title: 'Settings', icon: 'mdi-cog', path: '/settings' },
  { key: 'notes', title: 'Notes', icon: 'mdi-note-multiple', path: '/notes' },
]

const campaignStore = useCampaignStore()
const router = useRouter()

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
