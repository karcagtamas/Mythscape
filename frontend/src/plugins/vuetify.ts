// Styles
import '@mdi/font/css/materialdesignicons.css'
import '@/styles/main.scss'

// Vuetify
import { createVuetify, type ThemeDefinition } from 'vuetify'
import { en, fr, hu } from 'vuetify/locale'
import { aliases, mdi } from 'vuetify/iconsets/mdi'

const myLightTheme: ThemeDefinition = {
  dark: false,
  colors: {
    background: '#FFFFFF',
    surface: '#FFFFFF',
    primary: '#6200EE',
    'primary-darken-1': '#3700B3',
    secondary: '#03DAC6',
    'secondary-darken-1': '#018786',
    error: '#B00020',
    info: '#2196F3',
    success: '#4CAF50',
    warning: '#FB8C00',
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
})
