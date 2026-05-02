import request from '@/utils/request'

export const getDoctorList = (params?: any) => {
  return request.get('/doctor/list', { params })
}

export const createDoctor = (data: any) => {
  return request.post('/doctor', data)
}

export const updateDoctor = (id: number, data: any) => {
  return request.put(`/doctor/${id}`, data)
}

export const deleteDoctor = (id: number) => {
  return request.delete(`/doctor/${id}`)
}

export const resetDoctorPassword = (id: number) => {
  return request.post(`/doctor/${id}/reset-password`)
}
