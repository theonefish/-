import request from '@/utils/request'

export const getMenuList = () => {
  return request.get('/system/menu/list')
}

export const createMenu = (data: any) => {
  return request.post('/system/menu', data)
}

export const updateMenu = (id: number, data: any) => {
  return request.put(`/system/menu/${id}`, data)
}

export const deleteMenu = (id: number) => {
  return request.delete(`/system/menu/${id}`)
}

export const getRoleList = (params?: any) => {
  return request.get('/system/role/list', { params })
}

export const createRole = (data: any) => {
  return request.post('/system/role', data)
}

export const updateRole = (id: number, data: any) => {
  return request.put(`/system/role/${id}`, data)
}

export const deleteRole = (id: number) => {
  return request.delete(`/system/role/${id}`)
}

export const getNewsList = (params?: any) => {
  return request.get('/system/news/list', { params })
}

export const createNews = (data: any) => {
  return request.post('/system/news', data)
}

export const updateNews = (id: number, data: any) => {
  return request.put(`/system/news/${id}`, data)
}

export const deleteNews = (id: number) => {
  return request.delete(`/system/news/${id}`)
}

export const getNoticeList = (params?: any) => {
  return request.get('/system/notice/list', { params })
}

export const createNotice = (data: any) => {
  return request.post('/system/notice', data)
}

export const updateNotice = (id: number, data: any) => {
  return request.put(`/system/notice/${id}`, data)
}

export const deleteNotice = (id: number) => {
  return request.delete(`/system/notice/${id}`)
}

export const getFeedbackList = (params?: any) => {
  return request.get('/system/feedback/list', { params })
}

export const deleteFeedback = (id: number) => {
  return request.delete(`/system/feedback/${id}`)
}
