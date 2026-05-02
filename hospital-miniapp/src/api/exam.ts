import { request } from '@/utils/request'

export function getExamListByUser(userId: number) {
  return request<{ data: any[] }>({
    url: '/exam/user/' + userId,
    method: 'GET'
  })
}
