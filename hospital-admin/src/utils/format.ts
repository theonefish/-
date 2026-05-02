import dayjs from 'dayjs'

export const formatDate = (date: string | Date, format = 'YYYY-MM-DD HH:mm:ss') => {
  if (!date) return '-'
  return dayjs(date).format(format)
}

export const formatDateOnly = (date: string | Date) => {
  return formatDate(date, 'YYYY-MM-DD')
}

export const formatTime = (date: string | Date) => {
  return formatDate(date, 'HH:mm:ss')
}

export const formatGender = (gender: number) => {
  const map: Record<number, string> = { 0: '女', 1: '男', 2: '未知' }
  return map[gender] || '未知'
}

export const formatStatus = (status: number, type: 'default' | 'user' | 'doctor' = 'default') => {
  const maps: Record<string, Record<number, { text: string; type: 'success' | 'danger' | 'warning' | 'info' }>> = {
    default: {
      0: { text: '禁用', type: 'danger' },
      1: { text: '启用', type: 'success' }
    },
    user: {
      0: { text: '禁用', type: 'danger' },
      1: { text: '正常', type: 'success' }
    },
    doctor: {
      0: { text: '休息', type: 'info' },
      1: { text: '出诊', type: 'success' }
    }
  }
  return maps[type]?.[status] || { text: '未知', type: 'info' }
}
