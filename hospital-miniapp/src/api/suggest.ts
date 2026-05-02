import { request } from '@/utils/request'

export function submitSuggest(data: { userId: number; content: string }) {
  return request({
    url: '/suggest',
    method: 'POST',
    data
  })
}
