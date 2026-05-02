import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { UserInfo, MenuItem } from '@/types'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(null)
  const menus = ref<MenuItem[]>([])
  const permissions = ref<string[]>([])

  const isLoggedIn = computed(() => !!token.value)

  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    permissions.value = info.permissions || []
  }

  const setMenus = (menuList: MenuItem[]) => {
    menus.value = menuList
  }

  const hasPermission = (permission: string) => {
    return permissions.value.includes(permission) || permissions.value.includes('*')
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    menus.value = []
    permissions.value = []
    localStorage.removeItem('token')
  }

  return {
    token,
    userInfo,
    menus,
    permissions,
    isLoggedIn,
    setToken,
    setUserInfo,
    setMenus,
    hasPermission,
    logout
  }
})
