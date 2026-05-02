import { request } from '@/utils/request'

export function getLabListByUser(userId: number) {
  return request<{ data: any[] }>({
    url: '/lab/user/' + userId,
    method: 'GET'
  })
}
