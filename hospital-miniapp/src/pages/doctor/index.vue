<template>
  <view class="doctor-page">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <view class="search-input-wrap">
        <text class="search-icon">🔍</text>
        <input v-model="keyword" placeholder="搜索医生姓名" @confirm="loadDoctors" />
      </view>
    </view>

    <view class="container">
      <!-- 左侧科室 -->
      <scroll-view class="dept-sidebar" scroll-y>
        <view v-for="item in deptList" :key="item.deptId" class="dept-item"
          :class="{ active: currentDept === item.deptId }" @click="switchDept(item.deptId)">
          {{ item.deptName }}
        </view>
      </scroll-view>

      <!-- 右侧医生 -->
      <scroll-view class="doctor-list" scroll-y @scrolltolower="loadMore">
        <view v-for="item in doctorList" :key="getDoctorId(item)" class="doctor-card" @click="goDetail(item)">
          <view class="avatar-wrap">
            <image class="avatar" :src="item.image || '/static/default-avatar.png'" mode="aspectFill" />
          </view>
          <view class="info">
            <view class="row">
              <text class="name">{{ item.nickName || item.userName }}</text>
              <text class="title">{{ item.jobTitle || '医师' }}</text>
            </view>
            <text class="dept">{{ item.deptName || '科室' }}</text>
            <text class="specialty">{{ item.specialty || '擅长：常见病诊治' }}</text>
          </view>
          <view class="arrow-icon"></view>
        </view>
        <view v-if="!doctorList.length" class="empty">
          <text class="empty-icon">👨‍⚕️</text>
          <text class="empty-text">暂无医生</text>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getDepartmentList } from '@/api/department'
import { getDoctorList } from '@/api/doctor'
import { receiveTabParams } from '@/utils/navigation'

const keyword = ref('')
const deptList = ref<any[]>([{ deptId: 0, deptName: '全部' }])
const doctorList = ref<any[]>([])
const currentDept = ref(0)
const page = ref(1)
const pageSize = 10
const total = ref(0)

async function loadDepts() {
  const res = await getDepartmentList({ page: 1, pageSize: 100 })
  deptList.value = [{ deptId: 0, deptName: '全部' }, ...res.data.list]
}

async function loadDoctors(reset = true) {
  if (reset) page.value = 1
  const res = await getDoctorList({
    keyword: keyword.value,
    deptId: currentDept.value || undefined,
    page: page.value,
    pageSize
  })
  if (reset) {
    doctorList.value = res.data.list
  } else {
    doctorList.value.push(...res.data.list)
  }
  total.value = res.data.total
}

function switchDept(deptId: number) {
  currentDept.value = deptId
  loadDoctors(true)
}

function loadMore() {
  if (doctorList.value.length < total.value) {
    page.value++
    loadDoctors(false)
  }
}

// 获取医生ID - 兼容不同后端返回的字段名
function getDoctorId(item: any): number {
  return item.userId || item.doctorId || item.id || 0
}

function goDetail(item: any) {
  const id = getDoctorId(item)
  if (!id) {
    uni.showToast({ title: '医生ID无效', icon: 'none' })
    return
  }
  uni.navigateTo({ url: '/pages/doctor/detail?id=' + id })
}

onMounted(() => loadDepts().then(() => loadDoctors(true)))
onLoad((opt: any) => {
  // 处理从 navigateTo 直接跳转来的参数（非 tabBar 跳转）
  if (opt.deptId) {
    currentDept.value = Number(opt.deptId)
  }
  if (opt.keyword) {
    keyword.value = opt.keyword
  }
  // 处理从 switchTab（goTo）跳转来的参数，通过事件总线接收
  receiveTabParams('/pages/doctor/index', (params) => {
    if (params.deptId) {
      currentDept.value = Number(params.deptId)
      loadDoctors(true)
    }
    if (params.keyword !== undefined) {
      keyword.value = params.keyword
      loadDoctors(true)
    }
  })
})
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.doctor-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: $surface;
}

/* 搜索栏 */
.search-bar {
  padding: 20rpx 32rpx;
  background: $surface-container-lowest;
  border-bottom: 1rpx solid $surface-container;

  .search-input-wrap {
    display: flex;
    align-items: center;
    background: $surface-container;
    border-radius: 32rpx;
    padding: 16rpx 24rpx;
    gap: 12rpx;

    .search-icon {
      font-size: 28rpx;
      color: $on-surface-variant;
    }

    input {
      flex: 1;
      font-size: 28rpx;
      color: $on-surface;
    }
  }
}

.container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

/* 左侧科室栏 */
.dept-sidebar {
  width: 200rpx;
  background: $surface-container-low;

  .dept-item {
    padding: 32rpx 20rpx;
    font-size: 26rpx;
    color: $on-surface-variant;
    text-align: center;
    border-left: 6rpx solid transparent;
    transition: all 0.2s;

    &.active {
      background: $surface-container-lowest;
      color: $primary;
      border-left-color: $primary;
      font-weight: 600;
    }
  }
}

/* 右侧医生列表 */
.doctor-list {
  flex: 1;
  padding: 0 24rpx;
  background: $surface;

  .doctor-card {
    display: flex;
    align-items: center;
    padding: 28rpx 0;
    border-bottom: 1rpx solid $surface-container;
    transition: all 0.2s;

    &:active {
      opacity: 0.7;
    }

    .avatar-wrap {
      width: 120rpx;
      height: 120rpx;
      border-radius: 50%;
      overflow: hidden;
      border: 2rpx solid $surface-container-high;
      box-shadow: $shadow-card;
      flex-shrink: 0;

      .avatar {
        width: 100%;
        height: 100%;
      }
    }

    .info {
      flex: 1;
      margin-left: 24rpx;
      min-width: 0;

      .row {
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        gap: 12rpx;

        .name {
          font-size: 30rpx;
          color: $on-surface;
          font-weight: 600;
        }

        .title {
          font-size: 24rpx;
          color: $primary;
          background: $primary-fixed;
          padding: 4rpx 16rpx;
          border-radius: 8rpx;
          font-weight: 500;
        }
      }

      .dept {
        display: block;
        font-size: 26rpx;
        color: $on-surface-variant;
        margin-top: 10rpx;
      }

      .specialty {
        display: block;
        font-size: 24rpx;
        color: $on-surface-variant;
        margin-top: 8rpx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    .arrow-icon {
      width: 16rpx;
      height: 16rpx;
      border-top: 4rpx solid $outline;
      border-right: 4rpx solid $outline;
      transform: rotate(45deg);
      margin-left: 16rpx;
    }
  }
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 120rpx 40rpx;

  .empty-icon {
    font-size: 80rpx;
    margin-bottom: 16rpx;
    opacity: 0.4;
  }

  .empty-text {
    font-size: 28rpx;
    color: $on-surface-variant;
  }
}
</style>
