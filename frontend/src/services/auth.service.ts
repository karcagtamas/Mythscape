import type { LoginDTO, RegisterDTO, TokenDTO } from '@/models/auth'
import type { ServerResponse } from '@/models/response'
import axios from 'axios'

const API_URL = 'http://localhost:8080/auth'

class AuthService {
  public async login(dto: LoginDTO): Promise<ServerResponse<TokenDTO>['data']> {
    const res = await axios.post<ServerResponse<TokenDTO>>(`${API_URL}/login`, dto)
    if (res.data.data?.token) {
      localStorage.setItem('token', res.data.data.token)
    }
    return res.data.data
  }

  public logout(): void {
    localStorage.removeItem('token')
  }

  public async register(dto: RegisterDTO): Promise<void> {
    const res = await axios.post<ServerResponse<TokenDTO>>(`${API_URL}/login`, dto)
    if (res.data.data?.token) {
      localStorage.setItem('token', res.data.data.token)
    }
  }
}

export default new AuthService()
