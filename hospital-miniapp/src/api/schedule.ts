import { request } from '@/utils/request'

export function getScheduleList(params: { doctorId?: number; startDate?: string; endDate?: string; page?: number; pageSize?: number }) {
  return request<{ data: { list: any[]; total: number } }>({
    url: '/schedule/list',
    method: 'GET',
    data: params
  })
}
