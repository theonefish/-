import { request } from '@/utils/request'

export function login(data: { username: string; password: string }) {
  return request<{ data: { token: string; expires: number } }>({
    url: '/auth/login',
    method: 'POST',
    data
  })
}

export function wxLogin(data: { openId: string }) {
  return request<{ data: { token: string; expires: number } }>({
    url: '/auth/wx-login',
    method: 'POST',
    data
  })
}

export function wxRegister(data: any) {
  return request({
    url: '/auth/wx-register',
    method: 'POST',
    data
  })
}

export function getUserInfo() {
  return request<{ data: any }>({
    url: '/auth/info',
    method: 'GET'
  })
}

export function updatePassword(data: { oldPassword: string; newPassword: string }) {
  return request({
    url: '/auth/password',
    method: 'POST',
    data
  })
}
