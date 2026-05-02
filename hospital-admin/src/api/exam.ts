import request from '@/utils/request'

export const getExamList = (params?: any) => {
  return request.get('/exam/list', { params })
}

export const createExam = (data: any) => {
  return request.post('/exam', data)
}

export const updateExam = (id: number, data: any) => {
  return request.put(`/exam/${id}`, data)
}

export const uploadExamReport = (id: number, file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post(`/exam/${id}/report`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
