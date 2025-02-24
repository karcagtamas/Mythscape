import type { UserDTO } from './user'

export interface AuthState {
  user: UserDTO | null
  token: string
}

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
}
