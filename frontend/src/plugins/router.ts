import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/auth/LoginView.vue'
import RegisterView from '../views/auth/RegisterView.vue'
import AuthView from '../views/frame/AuthView.vue'
import DashboardView from '../views/DashboardView.vue'
import { useAuthStore } from '@/stores/auth.store'
import VersionsView from '@/views/VersionsView.vue'
import AppBaseView from '@/views/frame/AppBaseView.vue'
import BaseView from '@/views/frame/BaseView.vue'
import CampaignDataView from '@/views/campaigns/CampaignDataView.vue'
import CampaignBaseView from '@/views/campaigns/CampaignBaseView.vue'
import CampaignSettingsView from '@/views/campaigns/CampaignSettingsView.vue'
import CampaignNotesView from '@/views/campaigns/CampaignNotesView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/app',
      name: 'app',
      component: AppBaseView,
      meta: { requiresAuth: true },
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: DashboardView,
          meta: { requiresAuth: true },
        },
        {
          path: 'campaigns/:campaignId',
          name: 'campaign',
          component: CampaignBaseView,
          meta: { requiresAuth: true },
          children: [
            {
              path: '',
              name: 'campaign-data',
              component: CampaignDataView,
              meta: { requiresAuth: true, page: 'data' },
            },
            {
              path: 'settings',
              name: 'campaign-settings',
              component: CampaignSettingsView,
              meta: { requiresAuth: true, page: 'settings' },
            },
            {
              path: 'notes',
              name: 'campaign-notes',
              component: CampaignNotesView,
              meta: { requiresAuth: true, page: 'notes' },
            },
          ],
        },
      ],
      redirect: 'app/dashboard',
    },
    {
      path: '/',
      name: 'index',
      component: BaseView,
      children: [
        {
          path: '',
          name: 'home',
          component: HomeView,
        },
        {
          path: 'versions',
          name: 'versions',
          component: VersionsView,
        },
      ],
    },

    {
      path: '/auth',
      name: 'auth',
      component: AuthView,
      children: [
        { path: 'login', name: 'login', component: LoginView },
        { path: 'register', name: 'register', component: RegisterView },
      ],
    },
  ],
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.loggedIn) {
    next('/auth/login')
  } else {
    next()
  }
})

export default router
