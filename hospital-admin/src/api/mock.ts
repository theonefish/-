import type { UserInfo, MenuItem } from '@/types'

export const mockLogin = (data: { username: string; password: string }) => {
  return new Promise<{ token: string }>((resolve) => {
    setTimeout(() => {
      resolve({ token: 'mock_token_' + Date.now() })
    }, 500)
  })
}

export const mockUserInfo = (): UserInfo => ({
  id: 1,
  username: 'admin',
  nickname: '管理员',
  avatar: '',
  role: '超级管理员',
  roleId: 1,
  permissions: ['*']
})

export const mockMenus = (): MenuItem[] => [
  { id: 1, name: '首页', path: '/dashboard', component: 'dashboard/index', icon: 'HomeFilled', parentId: null, sort: 1, hidden: false },
  { id: 2, name: '科室管理', path: '/department', component: 'department/index', icon: 'OfficeBuilding', parentId: null, sort: 2, hidden: false },
  { id: 3, name: '医生管理', path: '/doctor', component: 'doctor/index', icon: 'UserFilled', parentId: null, sort: 3, hidden: false },
  { id: 4, name: '排班管理', path: '/schedule', component: 'schedule/index', icon: 'Calendar', parentId: null, sort: 4, hidden: false },
  { id: 5, name: '用户管理', path: '/user', component: 'user/index', icon: 'User', parentId: null, sort: 5, hidden: false },
  { id: 6, name: '预约管理', path: '/appointment', component: 'appointment/index', icon: 'Timer', parentId: null, sort: 6, hidden: false },
  { id: 7, name: '就诊管理', path: '/visit', component: 'visit/index', icon: 'FirstAidKit', parentId: null, sort: 7, hidden: false },
  { id: 8, name: '检查管理', path: '/exam', component: 'exam/index', icon: 'View', parentId: null, sort: 8, hidden: false },
  { id: 9, name: '检验管理', path: '/lab', component: 'lab/index', icon: 'Collection', parentId: null, sort: 9, hidden: false },
  { id: 10, name: '数据统计', path: '/statistics', component: 'statistics/index', icon: 'TrendCharts', parentId: null, sort: 10, hidden: false },
  {
    id: 11, name: '系统管理', path: '/system', component: '', icon: 'Setting', parentId: null, sort: 11, hidden: false,
    children: [
      { id: 111, name: '菜单管理', path: '/system/menu', component: 'system/menu', icon: '', parentId: 11, sort: 1, hidden: false },
      { id: 112, name: '角色管理', path: '/system/role', component: 'system/role', icon: '', parentId: 11, sort: 2, hidden: false },
      { id: 113, name: '资讯管理', path: '/system/news', component: 'system/news', icon: '', parentId: 11, sort: 3, hidden: false },
      { id: 114, name: '公告管理', path: '/system/notice', component: 'system/notice', icon: '', parentId: 11, sort: 4, hidden: false },
      { id: 115, name: '反馈管理', path: '/system/feedback', component: 'system/feedback', icon: '', parentId: 11, sort: 5, hidden: false }
    ]
  }
]
