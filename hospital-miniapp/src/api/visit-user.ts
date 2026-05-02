import { request } from '@/utils/request'

export function getVisitUserList(userId: number) {
  return request<{ data: any[] }>({
    url: '/visit-user/list/' + userId,
    method: 'GET'
  })
}

export function addVisitUser(data: any) {
  return request({
    url: '/visit-user',
    method: 'POST',
    data
  })
}

export function updateVisitUser(id: number, data: any) {
  return request({
    url: '/visit-user/' + id,
    method: 'PUT',
    data
  })
}

export function deleteVisitUser(id: number) {
  return request({
    url: '/visit-user/' + id,
    method: 'DELETE'
  })
}
