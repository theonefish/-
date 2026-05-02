import { request } from '@/utils/request'

export function getVisitListByUser(userId: number) {
  return request<{ data: any[] }>({
    url: '/visit/user/' + userId,
    method: 'GET'
  })
}
