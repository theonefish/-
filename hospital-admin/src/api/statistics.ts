import request from '@/utils/request'

export const getDashboardStats = () => {
  return request.get('/statistics/dashboard')
}

export const getAppointmentTrend = (params?: any) => {
  return request.get('/statistics/appointment-trend', { params })
}

export const getDeptAppointmentRatio = (params?: any) => {
  return request.get('/statistics/dept-ratio', { params })
}

export const getDoctorRanking = (params?: any) => {
  return request.get('/statistics/doctor-ranking', { params })
}
