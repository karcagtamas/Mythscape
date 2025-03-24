import { createApp } from 'vue'
import App from './App.vue'
import router from './plugins/router'
import vuetify from './plugins/vuetify'
import { loadFonts } from './plugins/webfontloader'
import pinia from './plugins/store'

loadFonts()

createApp(App).use(pinia).use(router).use(vuetify).mount('#app')
