import type { UserDTO } from './user'

export interface LoginDTO {
  username: string
  password: string
}

export interface RegisterDTO {
  username: string
  email: string
  password: string
  passwordConfirm: string
  fullname: string
}

export interface TokenDTO {
  token: string
  refreshToken: string
  clientId: string
  user: UserDTO
}

export interface RefreshDTO {
  refreshToken: string
  clientId: string
  userId: string
}
