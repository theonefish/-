import request from '@/utils/request'

export const login = (data: { username: string; password: string; captcha?: string }) => {
  return request.post('/auth/login', data)
}

export const getUserInfo = () => {
  return request.get('/auth/info')
}

export const getMenus = () => {
  return request.get('/auth/menus')
}

export const updatePassword = (data: { oldPassword: string; newPassword: string }) => {
  return request.post('/auth/password', data)
}
