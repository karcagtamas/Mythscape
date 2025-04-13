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
          class="d-flex text-center mx-auto mt-4 profile"
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
    <v-avatar
      v-for="campaign in campaigns"
      :key="campaign.id"
      class="d-block text-center mx-auto mb-9"
      color="grey-lighten-1"
      size="28"
    ></v-avatar>
    <v-divider class="mx-3 my-5"></v-divider>
    <v-avatar
      class="d-flex text-center mx-auto mt-4 profile"
      color="secondary"
      size="36"
      @click="handleAdd"
    >
      <v-icon>mdi-plus</v-icon>
    </v-avatar>
  </v-navigation-drawer>
  <v-navigation-drawer width="244">
    <v-sheet color="grey-lighten-5 text-center py-3" height="128" width="100%">
      <img class="main-color" src="/main.svg" height="100%" />
    </v-sheet>

    <v-list>
      <v-list-item v-for="n in 5" :key="n" :title="`Item ${n}`" link></v-list-item>
    </v-list>
  </v-navigation-drawer>

  <v-app-bar></v-app-bar>

  <router-view></router-view>

  <v-navigation-drawer location="right">
    <v-list>
      <v-list-item v-for="n in 5" :key="n" :title="`Item ${n}`" link></v-list-item>
    </v-list>
  </v-navigation-drawer>

  <v-footer height="72" app> </v-footer>
</template>

<script setup lang="ts">
import type { CampaignDTO } from '@/models/campaign'
import type { UserDTO } from '@/models/user'
import { useAuthStore } from '@/stores/auth.store'
import { useCampaignStore } from '@/stores/campaign.store'
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const campaignStore = useCampaignStore()
const router = useRouter()
const user = computed<UserDTO | null>(() => authStore.currentUser)
const campaigns = computed<CampaignDTO[]>(() => campaignStore.campaigns)

const toProfile = () => {
  router.push('/profile')
}

const toVersions = () => {
  router.push('/versions')
}

const handleAdd = () => {}
</script>

<style lang="scss">
.profile {
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
