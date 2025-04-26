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
        <v-avatar
          class="d-block text-center mx-auto mb-6 mt-6 item"
          color="grey-lighten-1"
          size="28"
          v-bind="props"
          @click="() => handleSelect(campaign)"
        ></v-avatar>
      </template>
    </v-tooltip>

    <v-divider class="mx-3 my-5"></v-divider>

    <v-tooltip text="Add campaign">
      <template v-slot:activator="{ props }">
        <v-avatar
          class="d-flex text-center mx-auto mt-4 item"
          color="secondary"
          size="36"
          v-bind="props"
          @click="handleAdd"
        >
          <v-icon>mdi-plus</v-icon>
        </v-avatar>
      </template>
    </v-tooltip>

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

onMounted(async () => {
  await campaignStore.fetchCampaign(authStore.user?.id ?? 0)
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

const handleAdd = () => {}

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
