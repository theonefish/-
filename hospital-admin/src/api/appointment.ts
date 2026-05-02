import request from '@/utils/request'

export const getAppointmentList = (params?: any) => {
  return request.get('/appointment/list', { params })
}

export const createAppointment = (data: any) => {
  return request.post('/appointment', data)
}

export const updateAppointment = (id: number, data: any) => {
  return request.put(`/appointment/${id}`, data)
}

export const deleteAppointment = (id: number) => {
  return request.delete(`/appointment/${id}`)
}

export const callNumber = (id: number) => {
  return request.post(`/appointment/${id}/call`)
}
