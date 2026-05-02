import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(uni.getStorageSync('token') || '')
  const userId = ref<number>(uni.getStorageSync('userId') || 0)
  const userInfo = ref<any>(null)

  const isLoggedIn = computed(() => !!token.value)

  function setLogin(data: { token: string; userId: number }) {
    token.value = data.token
    userId.value = data.userId
    uni.setStorageSync('token', data.token)
    uni.setStorageSync('userId', data.userId)
  }

  function setUserInfo(info: any) {
    userInfo.value = info
    uni.setStorageSync('userInfo', info)
  }

  function logout() {
    token.value = ''
    userId.value = 0
    userInfo.value = null
    uni.removeStorageSync('token')
    uni.removeStorageSync('userId')
    uni.removeStorageSync('userInfo')
  }

  function init() {
    const saved = uni.getStorageSync('userInfo')
    if (saved) userInfo.value = saved
  }

  return { token, userId, userInfo, isLoggedIn, setLogin, setUserInfo, logout, init }
})
