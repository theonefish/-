import { request } from '@/utils/request'

export function getDoctorList(params?: { keyword?: string; deptId?: number; page?: number; pageSize?: number; recommended?: number }) {
  return request<{ data: { list: any[]; total: number } }>({
    url: '/doctor/list',
    method: 'GET',
    data: params
  })
}

/**
 * 获取医生详情 - 使用公开接口，无需登录
 * @param id 医生ID
 */
export function getDoctorDetail(id: number) {
  return request<{ data: any }>({
    url: '/doctor/public/' + id,
    method: 'GET'
  })
}
