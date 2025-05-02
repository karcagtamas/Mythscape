<template>
  <v-system-bar>
    <span
      ><strong class="text-primary">Mythscape</strong> -
      <span class="versions" @click="toVersions">Latest version: 1.0.0</span></span
    >
    <v-spacer></v-spacer>
    <v-icon>mdi-square</v-icon>
    <v-icon>mdi-circle</v-icon>
    <v-icon>mdi-triangle</v-icon>
  </v-system-bar>
  <v-navigation-drawer color="grey-lighten-3" rail>
    <v-tooltip :text="user?.name">
      <template v-slot:activator="{ props }">
        <v-avatar
          class="d-flex text-center mx-auto mt-4 item"
          color="primary"
          size="36"
          v-bind="props"
          @click="toProfile"
        >
          <v-icon>mdi-account</v-icon>
        </v-avatar>
      </template>
    </v-tooltip>
    <v-divider v-if="campaigns.length" class="mx-3 my-5"></v-divider>
    <v-tooltip v-for="campaign in campaigns" :key="campaign.id" :text="campaign.name">
      <template v-slot:activator="{ props }">
        <TextAvatar
          v-if="selectedCampaign?.id === campaign.id"
          class="d-flex text-center mx-auto mb-6 mt-6 item"
          color="primary"
          :value="campaign.title"
          size="36"
          :text-size="16"
          v-bind="props"
          @click="() => handleSelect(campaign)"
        ></TextAvatar>

        <TextAvatar
          v-else
          class="d-flex text-center mx-auto mb-6 mt-6 item"
          color="grey-lighten-1"
          :value="campaign.title"
          size="28"
          :text-size="14"
          v-bind="props"
          @click="() => handleSelect(campaign)"
        ></TextAvatar>
      </template>
    </v-tooltip>

    <v-divider class="mx-3 my-5"></v-divider>

    <CampaignDialog mode="create" @save="handleAdd">
      <template v-slot:default="{ props: activatorProps }">
        <v-avatar
          class="d-flex text-center mx-auto mt-4 item"
          color="secondary"
          size="36"
          v-bind="activatorProps"
        >
          <v-icon>mdi-plus</v-icon>
        </v-avatar>
      </template>
    </CampaignDialog>

    <template v-slot:append>
      <v-tooltip text="Log out">
        <template v-slot:activator="{ props }">
          <v-avatar
            class="d-flex text-center mx-auto mb-4 item"
            color="primary"
            size="36"
            v-bind="props"
            @click="handleLogout"
          >
            <v-icon>mdi-logout</v-icon>
          </v-avatar>
        </template>
      </v-tooltip>
    </template>
  </v-navigation-drawer>

  <router-view></router-view>

  <v-footer height="72" app> </v-footer>
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
const handleAdd = async (id: number) => {
  await campaignStore.fetchCampaigns(authStore.userId)
}

const handleSelect = async (campaign: CampaignDTO) => {
  await campaignStore.select(campaign)
  router.push(`/app/campaigns/${campaign.id}`)
}
</script>

<style lang="scss">
.item {
  transition-duration: 0.4s;
  cursor: pointer;

  &:hover {
    opacity: 0.8;
  }
}

.versions {
  transition-duration: 0.4s;
  cursor: pointer;

  &:hover {
    opacity: 0.8;
  }
}
</style>
