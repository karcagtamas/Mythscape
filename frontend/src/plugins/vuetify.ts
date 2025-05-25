// Styles
import '@mdi/font/css/materialdesignicons.css'
import '@/styles/main.scss'

// Vuetify
import { createVuetify, type ThemeDefinition } from 'vuetify'
import { en, fr, hu } from 'vuetify/locale'
import { aliases, mdi } from 'vuetify/iconsets/mdi'
import { VTreeview } from 'vuetify/labs/VTreeview'
import DateFnsAdapter from '@date-io/date-fns'
import { enUS } from 'date-fns/locale'

const myLightTheme: ThemeDefinition = {
  dark: false,
  colors: {
    background: '#FFFFFF',
    surface: '#FFFFFF',
    primary: '#6C0202',
    secondary: '#02346C',
    accent: '#556C02',
    error: '#9F0000',
    info: '#008C9F',
    success: '#0E9F00',
    warning: '#9F4A00',
  },
}

export default createVuetify({
  theme: {
    defaultTheme: 'myLightTheme',
    themes: {
      myLightTheme,
    },
  },
  locale: {
    locale: 'en',
    fallback: 'en',
    messages: { en, fr, hu },
  },
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi,
    },
  },
  components: {
    VTreeview,
  },
  date: {
    adapter: DateFnsAdapter,
    locale: {
      en: enUS,
    },
  },
})
