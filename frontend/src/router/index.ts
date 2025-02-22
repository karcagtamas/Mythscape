import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/auth/LoginView.vue'
import RegisterView from '../views/auth/RegisterView.vue'
import AuthView from '../views/auth/AuthView.vue'
import CampaignsView from '../views/CampaignsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/campaigns',
      name: 'campaigns',
      component: CampaignsView,
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

export default router
