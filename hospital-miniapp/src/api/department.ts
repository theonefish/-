import { request } from '@/utils/request'

export function getDepartmentList(params?: { keyword?: string; page?: number; pageSize?: number; isIndex?: number }) {
  return request<{ data: { list: any[]; total: number } }>({
    url: '/department/list',
    method: 'GET',
    data: params
  })
}
