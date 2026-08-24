<template>
  <div class="h-screen w-screen flex flex-col overflow-hidden bg-background text-text">

    <!-- Topbar -->
    <div
      class="h-7 border-b border-border bg-surface/50 backdrop-blur-md px-4 flex items-center justify-between text-xs shrink-0 select-none z-20">
      <div>
        <strong class="text-primary font-bold tracking-wider uppercase">Mythscape</strong>
        <span class="mx-2 text-slate-600">|</span>
        <span @click="toVersions" class="text-slate-400 hiver:text-slate-200 transition cursor-pointer">
          Latest version: 1.0.0
        </span>
      </div>

      <div class="flex items-center gap-3 text-slate-500 scale-75 origin-right">
        <span class="w-3 h-3 border border-current rounded-sm"></span>
        <span class="w-3 h-3 border-2 border-current rounded-full"></span>
        <span
          class="w-0 h-0 border-l-[6px] border-l-transparent border-r-[6px] border-r-transparent border-b-[10px] border-b-current"></span>
      </div>
    </div>

    <!-- Workspace -->
    <div class="flex-1 flex min-h-0 w-full relative">
      <aside
        class="w-16 border-r border-border bg-surface/30 backdrop-blur flex flex-col items-center py-4 justify-between shrink-0 select-none z-10">
        <div class="w-full flex flex-col items-center gap-4">
          <div class="group relative">
            <button @click="toProfile"
              class="w-9 h-9 rounded-full bg-primary flex items-center justify-center text-white hover:opacity-80 transition-all active:scale-95 shadow-md shadow-primary/20">
              <span class="text-xs uppercase font-bold tracking-wide">{{ user?.name.slice(0, 2) || 'UI'
              }}</span></button>

            <!-- Tooltip -->
            <div
              class="absolute left-14 top-1/2 -translate-y-1/2 bg-zinc-950/90 border border-zinc-800 text-slate-200 text-[10px]">
              {{ user?.name || 'Profile Account' }}
            </div>
          </div>

          <div v-if="campaigns.length" class="w-10 border-b border-border/60 my-1"></div>

          <div v-for="campaign in campaigns" :key="campaign.id" class="group relative">
            <TextAvatar v-if="selectedCampaign?.id === campaign.id"
              class="w-9 h-9 rounded-full bg-primary text-white border-2 border-primary flex items-center justify-center cursor-pointer hover:opacity-80 transition active:scale-95 shadow-lg shadow-primary/20"
              :value="campaign.title" :size="36" :text-size="16" @click="handleSelect(campaign)" color="primary" />

            <TextAvatar v-else
              class="w-8 h-8 rounded-full bg-zinc-800 text-slate-400 flex items-center justify-center cursor-pointer hover:bg-zinc-700 hover:text-slate-200 transition active:scale-95"
              :value="campaign.title" :size="28" :text-size="14" @click="handleSelect(campaign)" color="grey-lighten-1" />

            <!-- Tooltip -->
            <div
              class="absolute left-14 top-1/2 -translate-y-1/2 bg-zinc-950/90 border border-zinc-800 text-slate-200 text-[10px] font-medium tracking-wide uppercase px-2 py-1 rounded shadow-xl opacity-0 scale-95 pointer-events-none group-hover:opacity-100 group-hover:scale-100 transition-all origin-left z-50 whitespace-nowrap">
              {{ campaign.name }}
            </div>
          </div>

          <div class="w-10 border-b border-border/60 my-1"></div>

          <CampaignDialog :campaign="null" @save="handleAdd">
            <template #default="{ props: activatorProps }">
              <div class="group relative">
                <button v-bind="activatorProps"
                  class="w-9 h-9 rounded-full bg-secondary flex items-center justify-center text-white hover:opacity-80 transition active:scale-95 shadow-md shadow-secondary/10 text-xl font-light">
                  +
                </button>
                <div
                  class="absolute left-14 top-1/2 -translate-y-1/2 bg-zinc-950/90 border border-zinc-800 text-slate-200 text-[10px] font-medium tracking-wide uppercase px-2 py-1 rounded shadow-xl opacity-0 scale-95 pointer-events-none group-hover:opacity-100 group-hover:scale-100 transition-all origin-left z-50 whitespace-nowrap">
                  Create Campaign
                </div>
              </div>
            </template>
          </CampaignDialog>
        </div>

        <!-- Sticky Navigation Base Append Block -->
        <div class="group relative">
          <button @click="handleLogout"
            class="w-9 h-9 rounded-full bg-zinc-900 border border-border flex items-center justify-center text-rose-400 hover:bg-rose-950/20 hover:border-rose-500/30 hover:text-rose-400 transition-all active:scale-95 shadow-sm">
            <!-- Clean unicode character fallback replacing old mdi-logout glyph strings -->
            <span class="text-sm font-semibold pl-0.5">➔</span>
          </button>
          <div
            class="absolute left-14 top-1/2 -translate-y-1/2 bg-zinc-950/90 border border-zinc-800 text-rose-400 text-[10px] font-medium tracking-wide uppercase px-2 py-1 rounded shadow-xl opacity-0 scale-95 pointer-events-none group-hover:opacity-100 group-hover:scale-100 transition-all origin-left z-50 whitespace-nowrap">
            Log Out
          </div>
        </div>
      </aside>

      <main class="flex-1 min-h-0 bg-background overflow-hidden relative">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import TextAvatar from '@/components/TextAvatar.vue'
import CampaignDialog from '@/components/campaigns/CampaignDialog.vue'
import type { CampaignDTO } from '@/models/campaign'
import type { UserDTO } from '@/models/user'
import { useAuthStore } from '@/stores/auth.store'
import { useCampaignStore } from '@/stores/campaign.store'
import { useCommonStore } from '@/stores/common.store'
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const store = useCommonStore()
const authStore = useAuthStore()
const campaignStore = useCampaignStore()
const router = useRouter()

const user = computed<UserDTO | null>(() => authStore.currentUser)
const campaigns = computed<CampaignDTO[]>(() => campaignStore.campaigns)
const selectedCampaign = computed<CampaignDTO | null>(() => campaignStore.current)

onMounted(async () => {
  await campaignStore.fetchCampaigns(authStore.user?.id ?? 0)
})

const toProfile = () => {
  router.push('/profile')
}

const toVersions = () => {
  router.push('/versions')
}

const handleLogout = () => {
  authStore.logout()
  store.setMessage({ text: 'You successfully logged out', type: 'success' })
  router.push('/')
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
const handleAdd = async (id: number | null) => {
  await campaignStore.fetchCampaigns(authStore.userId)
}

const handleSelect = async (campaign: CampaignDTO) => {
  await campaignStore.select(campaign)
  router.push(`/app/campaigns/${campaign.id}`)
}
</script>
