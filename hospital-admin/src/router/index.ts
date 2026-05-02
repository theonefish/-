import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { public: true }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'HomeFilled', permission: 'dashboard:view' }
      },
      {
        path: 'department',
        name: 'Department',
        component: () => import('@/views/department/index.vue'),
        meta: { title: '科室管理', icon: 'OfficeBuilding', permission: 'department:view' }
      },
      {
        path: 'doctor',
        name: 'Doctor',
        component: () => import('@/views/doctor/index.vue'),
        meta: { title: '医生管理', icon: 'UserFilled', permission: 'doctor:view' }
      },
      {
        path: 'schedule',
        name: 'Schedule',
        component: () => import('@/views/schedule/index.vue'),
        meta: { title: '排班管理', icon: 'Calendar', permission: 'schedule:view' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/index.vue'),
        meta: { title: '用户管理', icon: 'User', permission: 'user:view' }
      },
      {
        path: 'appointment',
        name: 'Appointment',
        component: () => import('@/views/appointment/index.vue'),
        meta: { title: '预约管理', icon: 'Timer', permission: 'appointment:view' }
      },
      {
        path: 'visit',
        name: 'Visit',
        component: () => import('@/views/visit/index.vue'),
        meta: { title: '就诊管理', icon: 'FirstAidKit', permission: 'visit:view' }
      },
      {
        path: 'exam',
        name: 'Exam',
        component: () => import('@/views/exam/index.vue'),
        meta: { title: '检查管理', icon: 'View', permission: 'exam:view' }
      },
      {
        path: 'lab',
        name: 'Lab',
        component: () => import('@/views/lab/index.vue'),
        meta: { title: '检验管理', icon: 'Collection', permission: 'lab:view' }
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/statistics/index.vue'),
        meta: { title: '数据统计', icon: 'TrendCharts', permission: 'statistics:view' }
      },
      {
        path: 'system',
        name: 'System',
        meta: { title: '系统管理', icon: 'Setting' },
        children: [
          {
            path: 'menu',
            name: 'Menu',
            component: () => import('@/views/system/menu.vue'),
            meta: { title: '菜单管理', permission: 'system:menu:view' }
          },
          {
            path: 'role',
            name: 'Role',
            component: () => import('@/views/system/role.vue'),
            meta: { title: '角色管理', permission: 'system:role:view' }
          },
          {
            path: 'news',
            name: 'News',
            component: () => import('@/views/system/news.vue'),
            meta: { title: '资讯管理', permission: 'system:news:view' }
          },
          {
            path: 'notice',
            name: 'Notice',
            component: () => import('@/views/system/notice.vue'),
            meta: { title: '公告管理', permission: 'system:notice:view' }
          },
          {
            path: 'feedback',
            name: 'Feedback',
            component: () => import('@/views/system/feedback.vue'),
            meta: { title: '反馈管理', permission: 'system:feedback:view' }
          }
        ]
      }
    ]
  },
  {
    path: '/403',
    name: 'Forbidden',
    component: () => import('@/views/error/403.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.meta?.public) {
    next()
    return
  }

  if (!userStore.isLoggedIn) {
    next('/login')
    return
  }

  const requiredPermission = to.meta?.permission as string
  if (requiredPermission && !userStore.hasPermission(requiredPermission)) {
    next('/403')
    return
  }

  next()
})

export default router
