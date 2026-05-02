import request from '@/utils/request'

export const getLabList = (params?: any) => {
  return request.get('/lab/list', { params })
}

export const createLab = (data: any) => {
  return request.post('/lab', data)
}

export const updateLab = (id: number, data: any) => {
  return request.put(`/lab/${id}`, data)
}

export const uploadLabReport = (id: number, file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post(`/lab/${id}/report`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
