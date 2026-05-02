import request from '@/utils/request'

export const getScheduleList = (params?: any) => {
  return request.get('/schedule/list', { params })
}

export const createSchedule = (data: any) => {
  return request.post('/schedule', data)
}

export const updateSchedule = (id: number, data: any) => {
  return request.put(`/schedule/${id}`, data)
}

export const deleteSchedule = (id: number) => {
  return request.delete(`/schedule/${id}`)
}

export const batchCreateSchedule = (data: any[]) => {
  return request.post('/schedule/batch', data)
}

export const batchDeleteSchedule = (ids: number[]) => {
  return request.post('/schedule/batch-delete', { ids })
}
