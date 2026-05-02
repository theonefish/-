import { request } from '@/utils/request'

export function getWxUserInfo(id: number) {
  return request<{ data: any }>({
    url: '/wx/user/info/' + id,
    method: 'GET'
  })
}

export function updateWxUser(id: number, data: any) {
  return request({
    url: '/wx/user/' + id,
    method: 'PUT',
    data
  })
}
