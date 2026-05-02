<template>
  <div v-if="!item.meta?.hidden">
    <el-sub-menu v-if="item.children && item.children.length > 0" :index="item.path">
      <template #title>
        <el-icon v-if="item.meta?.icon"><component :is="item.meta.icon" /></el-icon>
        <span>{{ item.meta?.title }}</span>
      </template>
      <SidebarItem
        v-for="child in item.children"
        :key="child.path"
        :item="child"
      />
    </el-sub-menu>
    <el-menu-item v-else :index="item.path">
      <el-icon v-if="item.meta?.icon"><component :is="item.meta.icon" /></el-icon>
      <template #title>
        <span>{{ item.meta?.title }}</span>
      </template>
    </el-menu-item>
  </div>
</template>

<script setup lang="ts">
import type { RouteRecordRaw } from 'vue-router'

defineProps<{
  item: RouteRecordRaw
}>()
</script>
