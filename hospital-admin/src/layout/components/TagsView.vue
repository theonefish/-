<template>
  <div class="tags-view">
    <el-tag
      v-for="tag in appStore.tagsView"
      :key="tag.path"
      :closable="tag.path !== '/dashboard'"
      :effect="route.path === tag.path ? 'dark' : 'plain'"
      @click="router.push(tag.path)"
      @close="appStore.removeTag(tag.path)"
      class="tag-item"
    >
      {{ tag.title }}
    </el-tag>
  </div>
</template>

<script setup lang="ts">
import { watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()

watch(
  () => route.path,
  () => {
    if (route.meta?.title) {
      appStore.addTag({
        name: route.name as string,
        path: route.path,
        title: route.meta.title as string
      })
    }
  },
  { immediate: true }
)
</script>

<style scoped lang="scss">
.tags-view {
  .tag-item {
    margin-right: 8px;
    cursor: pointer;
  }
}
</style>
