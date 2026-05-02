<template>
  <aside class="sidebar" :class="{ collapsed: appStore.sidebarCollapsed }">
    <div class="logo">
      <el-icon size="28"><FirstAidKit /></el-icon>
      <span v-show="!appStore.sidebarCollapsed">医院管理系统</span>
    </div>
    <el-scrollbar>
      <el-menu
        :default-active="activeMenu"
        :collapse="appStore.sidebarCollapsed"
        :collapse-transition="false"
        router
        background-color="#001529"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <SidebarItem v-for="route in menuRoutes" :key="route.path" :item="route" />
      </el-menu>
    </el-scrollbar>
  </aside>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import router from '@/router'
import SidebarItem from './SidebarItem.vue'
import type { RouteRecordRaw } from 'vue-router'

const route = useRoute()
const appStore = useAppStore()

const activeMenu = computed(() => route.path)

const menuRoutes = computed(() => {
  const routes = router.getRoutes()
  const layout = routes.find((r: RouteRecordRaw) => r.path === '/')
  return (layout?.children as RouteRecordRaw[]) || []
})
</script>

<style scoped lang="scss">
.logo {
  .el-icon {
    color: #409eff;
  }
}
</style>
