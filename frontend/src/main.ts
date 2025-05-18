import { createApp } from 'vue'
import App from './App.vue'
import router from './plugins/router'
import vuetify from './plugins/vuetify'
import { loadFonts } from './plugins/webfontloader'
import pinia from './plugins/store'
import { install as VueMonacoEditorPlugin } from '@guolao/vue-monaco-editor'

loadFonts()

createApp(App)
  .use(pinia)
  .use(router)
  .use(vuetify)
  .use(VueMonacoEditorPlugin, {
    paths: {
      vs: 'https://cdn.jsdelivr.net/npm/monaco-editor@0.52.2/min/vs',
    },
  })
  .mount('#app')
