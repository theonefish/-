import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  const sidebarCollapsed = ref(false)
  const theme = ref(localStorage.getItem('theme') || 'light')
  const tagsView = ref<{ name: string; path: string; title: string }[]>([])

  const toggleSidebar = () => {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  const setTheme = (newTheme: string) => {
    theme.value = newTheme
    localStorage.setItem('theme', newTheme)
    document.documentElement.setAttribute('data-theme', newTheme)
  }

  const addTag = (tag: { name: string; path: string; title: string }) => {
    const exists = tagsView.value.find(t => t.path === tag.path)
    if (!exists) {
      tagsView.value.push(tag)
    }
  }

  const removeTag = (path: string) => {
    const index = tagsView.value.findIndex(t => t.path === path)
    if (index > -1) {
      tagsView.value.splice(index, 1)
    }
  }

  return {
    sidebarCollapsed,
    theme,
    tagsView,
    toggleSidebar,
    setTheme,
    addTag,
    removeTag
  }
})
