import { request } from '@/utils/request'

export function getNewsList(params?: { keyword?: string; page?: number; pageSize?: number; type?: string }) {
  return request<{ data: { list: any[]; total: number } }>({
    url: '/news/list',
    method: 'GET',
    data: params
  })
}
