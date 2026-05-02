import request from '@/utils/request'

export const getDeptList = (params?: any) => {
  return request.get('/department/list', { params })
}

export const createDept = (data: any) => {
  return request.post('/department', data)
}

export const updateDept = (id: number, data: any) => {
  return request.put(`/department/${id}`, data)
}

export const deleteDept = (id: number) => {
  return request.delete(`/department/${id}`)
}
