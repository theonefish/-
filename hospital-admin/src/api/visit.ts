import request from '@/utils/request'

export const getVisitList = (params?: any) => {
  return request.get('/visit/list', { params })
}

export const createVisit = (data: any) => {
  return request.post('/visit', data)
}

export const updateVisit = (id: number, data: any) => {
  return request.put(`/visit/${id}`, data)
}

export const getVisitDetail = (id: number) => {
  return request.get(`/visit/${id}`)
}
