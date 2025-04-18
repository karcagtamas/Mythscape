export const SERVER_URL = import.meta.env.VITE_APP_URL
export const API_URL = `${SERVER_URL}/api`

export type Version = {
  release: string
  date: Date
  changes: string[]
}

export const VERSIONS: Version[] = [
  {
    release: '1.0.0',
    date: new Date(),
    changes: ['User authentication', 'Index page', 'Versions page', 'Registrations'],
  },
]
