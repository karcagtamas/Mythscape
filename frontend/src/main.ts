import { createApp } from 'vue'
import App from './App.vue'
import router from './plugins/router'
import pinia from './plugins/store'
import { install as VueMonacoEditorPlugin } from '@guolao/vue-monaco-editor'

import './assets/main.css'

createApp(App)
  .use(pinia)
  .use(router)
  .use(VueMonacoEditorPlugin, {
    paths: {
      vs: 'https://cdn.jsdelivr.net/npm/monaco-editor@0.52.2/min/vs',
    },
  })
  .mount('#app')
