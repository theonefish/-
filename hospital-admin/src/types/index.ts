export interface UserInfo {
  id: number
  username: string
  nickname: string
  avatar: string
  role: string
  roleId: number
  permissions: string[]
}

export interface MenuItem {
  id: number
  name: string
  path: string
  component?: string
  icon?: string
  parentId: number | null
  sort: number
  hidden: boolean
  children?: MenuItem[]
}

export interface Role {
  id: number
  name: string
  code: string
  description: string
  menuIds: number[]
  status: number
}

export interface Dept {
  id: number
  name: string
  description: string
  phone: string
  recommended: boolean
  status: number
  sort: number
}

export interface Doctor {
  id: number
  name: string
  title: string
  deptId: number
  deptName: string
  avatar: string
  specialty: string
  introduction: string
  status: number
}

export interface Schedule {
  id: number
  doctorId: number
  doctorName: string
  deptName: string
  date: string
  period: string
  quota: number
  booked: number
  status: number
}

export interface Patient {
  id: number
  name: string
  phone: string
  idCard: string
  gender: number
  age: number
  status: number
  createTime: string
}

export interface Appointment {
  id: number
  patientName: string
  patientPhone: string
  doctorName: string
  deptName: string
  date: string
  period: string
  status: number
  createTime: string
}

export interface Visit {
  id: number
  patientName: string
  doctorName: string
  deptName: string
  diagnosis: string
  advice: string
  medication?: string
  visitTime: string
  status: number
}

export interface Exam {
  id: number
  patientName: string
  doctorName: string
  examType: string
  result: string
  reportUrl: string
  status: number
  createTime: string
}

export interface Lab {
  id: number
  patientName: string
  doctorName: string
  labType: string
  result: string
  reportUrl: string
  status: number
  createTime: string
}

export interface News {
  id: number
  title: string
  content: string
  cover: string
  recommended: boolean
  status: number
  createTime: string
}

export interface Notice {
  id: number
  title: string
  content: string
  showHome: boolean
  status: number
  createTime: string
}

export interface Feedback {
  id: number
  userName: string
  content: string
  contact: string
  status: number
  createTime: string
}
