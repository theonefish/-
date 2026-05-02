<template>
  <view class="index-page">
    <!-- 顶部导航栏 -->
    <view class="header-bar">
      <view class="location">
        <text class="icon">📍</text>
        <text class="text">康华医院</text>
        <text class="arrow">▼</text>
      </view>
      <view class="search-box" @click="onSearch">
        <text class="search-icon">🔍</text>
        <text class="search-placeholder">搜索医生、科室、疾病</text>
      </view>
      <text class="scan-icon">📷</text>
    </view>

    <!-- 轮播 Banner -->
    <swiper class="banner-swiper" indicator-dots autoplay circular interval="3000" @change="onBannerChange"
      indicator-color="rgba(255,255,255,0.4)" indicator-active-color="#ffffff">
      <swiper-item v-for="(item, idx) in banners" :key="idx" @click="onBannerClick(item)">
        <view class="banner-item" :style="{ background: item.bg }">
          <view class="banner-content">
            <text class="banner-title">{{ item.title }}</text>
            <text class="banner-sub">{{ item.sub }}</text>
            <view class="banner-btn" v-if="item.btnText">{{ item.btnText }}</view>
          </view>
        </view>
      </swiper-item>
    </swiper>

    <!-- 快捷功能入口 -->
    <view class="quick-grid">
      <view class="quick-item" v-for="(item, idx) in quickList" :key="idx" @click="item.action">
        <view class="icon-wrap" :style="{ background: item.bg }">
          <text class="icon">{{ item.icon }}</text>
        </view>
        <text class="label">{{ item.label }}</text>
      </view>
    </view>

    <!-- 特色科室 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">特色科室</text>
        <view class="more" @click="goDoctor">
          <text>查看全部</text>
          <view class="arrow-icon"></view>
        </view>
      </view>
      <view class="dept-grid">
        <view class="dept-card" v-for="item in deptList" :key="item.deptId" @click="goDoctorByDept(item.deptId)">
          <view class="dept-icon" :style="{ background: item.iconBg || '#e8f2ff' }">
            <text>{{ item.icon || '🏥' }}</text>
          </view>
          <view class="dept-info">
            <text class="dept-name">{{ item.deptName }}</text>
            <text class="dept-desc">{{ item.deptDesc || '专业诊疗' }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 健康资讯 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">健康资讯</text>
      </view>
      <view class="news-tabs">
        <view class="tab-item" v-for="tab in newsTabs" :key="tab.key"
          :class="{ active: currentTab === tab.key }" @click="switchNewsTab(tab.key)">
          {{ tab.label }}
        </view>
      </view>
      <view class="news-list">
        <view class="news-card" v-for="item in newsList" :key="item.newsId" @click="goNewsDetail(item)">
          <view class="news-content">
            <text class="news-tag" :class="item.tagClass">{{ item.tag }}</text>
            <text class="news-title">{{ item.title }}</text>
            <view class="news-meta">
              <text>{{ item.source }}</text>
              <text class="dot">·</text>
              <text>{{ item.time }}</text>
            </view>
          </view>
          <image class="news-image" :src="item.image || '/static/news-default.png'" mode="aspectFill" />
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getDepartmentList } from '@/api/department'
import { getDoctorList } from '@/api/doctor'
import { getNewsList } from '@/api/news'
import { useUserStore } from '@/stores/user'
import { goTo } from '@/utils/navigation'

// 轮播图数据
const banners = ref([
  {
    title: '温馨医疗 环境优美',
    sub: '专业团队 贴心服务',
    bg: 'linear-gradient(135deg, #0052d9, #003da6)',
    btnText: '了解详情',
    linkType: 'page',
    linkUrl: '/pages/news/index'
  },
  {
    title: '专家坐诊 预约从速',
    sub: '三甲医院专家定期坐诊',
    bg: 'linear-gradient(135deg, #006c46, #005234)',
    btnText: '立即预约',
    linkType: 'page',
    linkUrl: '/pages/doctor/index'
  },
  {
    title: '健康体检 关爱家人',
    sub: '全面体检套餐优惠中',
    bg: 'linear-gradient(135deg, #822600, #aa3500)',
    btnText: '查看套餐',
    linkType: 'page',
    linkUrl: '/pages/mine/index'
  }
])

function onBannerClick(item: any) {
  if (!item.linkType || !item.linkUrl) return
  if (item.linkType === 'page') {
    goTo(item.linkUrl)
  } else if (item.linkType === 'miniapp') {
    uni.navigateToMiniProgram({
      appId: item.appId,
      path: item.linkUrl,
      fail: () => uni.showToast({ title: '跳转失败', icon: 'none' })
    })
  }
}

function onBannerChange(e: any) {
  console.log('轮播切换到第', e.detail.current + 1, '张')
}

// 快捷入口
const quickList = ref([
  { icon: '📅', label: '预约挂号', bg: '#e8f2ff', action: goDoctor },
  { icon: '💳', label: '门诊缴费', bg: '#e6f7ed', action: goPay },
  { icon: '📋', label: '报告查询', bg: '#fff2e8', action: goReport },
  { icon: '🧪', label: '核酸检测', bg: '#fce8ec', action: goNucleic },
  { icon: '🩺', label: '互联网问诊', bg: '#e8f2ff', action: goOnline },
  { icon: '🏥', label: '住院服务', bg: '#e6f7ed', action: goHospital },
  { icon: '💉', label: '疫苗接种', bg: '#fff2e8', action: goVaccine },
  { icon: '⋮⋮', label: '全部', bg: '#f5f5f5', action: goAll }
])

// 科室列表
const deptList = ref<any[]>([])

// 资讯 Tab
const currentTab = ref('notice')
const newsTabs = ref([
  { key: 'notice', label: '最新公告' },
  { key: 'health', label: '健康科普' }
])
const newsList = ref<any[]>([])

// 加载首页数据
async function loadData() {
  try {
    const [deptRes, newsRes] = await Promise.all([
      getDepartmentList({ page: 1, pageSize: 4, isIndex: 1 }),
      getNewsList({ page: 1, pageSize: 4, type: currentTab.value })
    ])
    deptList.value = (deptRes.data.list || []).map((item: any, idx: number) => ({
      ...item,
      icon: ['❤️', '😊', '🤰', '👁️'][idx] || '🏥',
      iconBg: ['#fce8ec', '#e6f7ed', '#fff2e8', '#e8f2ff'][idx] || '#f5f5f5'
    }))
    newsList.value = formatNewsList(newsRes.data.list || [])
  } catch (e) {
    console.error(e)
  }
}

function formatNewsList(list: any[]) {
  return list.map((item: any) => ({
    ...item,
    tag: item.type === 'health' ? '健康科普' : '重要公告',
    tagClass: item.type === 'health' ? 'tag-health' : 'tag-important',
    source: item.source || '康华医院院办',
    time: item.time || '2小时前'
  }))
}

async function switchNewsTab(tabKey: string) {
  if (currentTab.value === tabKey) return
  currentTab.value = tabKey
  try {
    const res = await getNewsList({ page: 1, pageSize: 4, type: tabKey })
    newsList.value = formatNewsList(res.data.list || [])
  } catch (e) {
    console.error(e)
  }
}

const userStore = useUserStore()

function checkLogin() {
  if (!userStore.isLoggedIn) {
    uni.showModal({
      title: '提示',
      content: '该功能需要登录后才能使用',
      confirmText: '去登录',
      cancelText: '取消',
      success: (res) => {
        if (res.confirm) {
          goTo('/pages/login/index')
        }
      }
    })
    return false
  }
  return true
}

function onSearch() {
  goTo('/pages/doctor/index?keyword=')
}
function goDoctor() { uni.switchTab({ url: '/pages/doctor/index' }) }
function goDoctorByDept(deptId: number) { goTo('/pages/doctor/index?deptId=' + deptId) }
function goNewsDetail(item: any) { uni.navigateTo({ url: '/pages/news/detail?item=' + encodeURIComponent(JSON.stringify(item)) }) }
function goPay() { if (!checkLogin()) return; uni.showToast({ title: '功能开发中', icon: 'none' }) }
function goReport() { if (!checkLogin()) return; goTo('/pages/my-appointment/list') }
function goNucleic() { if (!checkLogin()) return; uni.showToast({ title: '功能开发中', icon: 'none' }) }
function goOnline() { if (!checkLogin()) return; uni.showToast({ title: '功能开发中', icon: 'none' }) }
function goHospital() { if (!checkLogin()) return; uni.showToast({ title: '功能开发中', icon: 'none' }) }
function goVaccine() { if (!checkLogin()) return; uni.showToast({ title: '功能开发中', icon: 'none' }) }
function goAll() { if (!checkLogin()) return; uni.showToast({ title: '功能开发中', icon: 'none' }) }

onMounted(() => loadData())
onShow(() => loadData())
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.index-page {
  min-height: 100vh;
  background: $surface;
  padding-bottom: 40rpx;
}

/* 顶部导航栏 */
.header-bar {
  display: flex;
  align-items: center;
  padding: 20rpx 32rpx;
  background: $surface-container-lowest;
  gap: 16rpx;

  .location {
    display: flex;
    align-items: center;
    gap: 6rpx;
    flex-shrink: 0;

    .icon { font-size: 28rpx; }
    .text {
      font-size: 28rpx;
      font-weight: 700;
      color: $on-surface;
    }
    .arrow {
      font-size: 20rpx;
      color: $on-surface-variant;
    }
  }

  .search-box {
    flex: 1;
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
    .search-placeholder {
      font-size: 26rpx;
      color: $on-surface-variant;
    }
  }

  .scan-icon {
    font-size: 36rpx;
    padding: 8rpx;
  }
}

/* 轮播 Banner */
.banner-swiper {
  height: 300rpx;
  margin: 24rpx 32rpx;
  border-radius: 24rpx;
  overflow: hidden;

  .banner-item {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    padding: 40rpx;
    color: $on-primary;
    position: relative;

    .banner-content {
      position: relative;
      z-index: 1;
    }

    .banner-title {
      font-size: 36rpx;
      font-weight: 700;
      text-shadow: 0 2rpx 8rpx rgba(0,0,0,0.2);
    }
    .banner-sub {
      font-size: 26rpx;
      margin-top: 12rpx;
      opacity: 0.9;
    }
    .banner-btn {
      margin-top: 20rpx;
      padding: 12rpx 32rpx;
      background: rgba(255, 255, 255, 0.25);
      border: 1rpx solid rgba(255, 255, 255, 0.5);
      border-radius: 32rpx;
      font-size: 26rpx;
      color: #fff;
      display: inline-flex;
      align-self: flex-start;
      backdrop-filter: blur(8rpx);
    }
  }
}

/* 快捷功能入口 */
.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24rpx;
  padding: 24rpx 32rpx;
  background: $surface-container-lowest;
  margin: 0 32rpx;
  border-radius: 24rpx;
  box-shadow: $shadow-card;

  .quick-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12rpx;

    .icon-wrap {
      width: 96rpx;
      height: 96rpx;
      border-radius: 24rpx;
      display: flex;
      align-items: center;
      justify-content: center;

      .icon {
        font-size: 44rpx;
      }
    }
    .label {
      font-size: 24rpx;
      color: $on-surface;
      font-weight: 500;
    }
  }
}

/* 区块通用样式 */
.section {
  margin: 32rpx 32rpx 0;
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 32rpx;
  box-shadow: $shadow-card;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24rpx;

    .section-title {
      font-size: 32rpx;
      font-weight: 700;
      color: $on-surface;
    }
    .more {
      display: flex;
      align-items: center;
      gap: 4rpx;
      font-size: 26rpx;
      color: $primary;
      font-weight: 500;

      .arrow-icon {
        width: 16rpx;
        height: 16rpx;
        border-top: 4rpx solid $primary;
        border-right: 4rpx solid $primary;
        transform: rotate(45deg);
      }
    }
  }
}

/* 特色科室 */
.dept-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;

  .dept-card {
    display: flex;
    align-items: center;
    gap: 20rpx;
    padding: 24rpx;
    background: $surface;
    border-radius: 20rpx;
    border: 1rpx solid $outline-variant;
    transition: all 0.2s;

    &:active {
      transform: scale(0.98);
      box-shadow: $shadow-card;
    }

    .dept-icon {
      width: 80rpx;
      height: 80rpx;
      border-radius: 20rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 36rpx;
      flex-shrink: 0;
    }
    .dept-info {
      flex: 1;
      min-width: 0;

      .dept-name {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: $on-surface;
      }
      .dept-desc {
        display: block;
        font-size: 24rpx;
        color: $on-surface-variant;
        margin-top: 8rpx;
      }
    }
  }
}

/* 资讯 Tab */
.news-tabs {
  display: flex;
  gap: 24rpx;
  margin-bottom: 24rpx;

  .tab-item {
    padding: 12rpx 28rpx;
    border-radius: 32rpx;
    font-size: 26rpx;
    color: $on-surface-variant;
    background: $surface-container;
    transition: all 0.2s;

    &.active {
      background: $primary;
      color: $on-primary;
      font-weight: 600;
    }
  }
}

/* 资讯列表 */
.news-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;

  .news-card {
    display: flex;
    gap: 24rpx;
    padding: 20rpx 0;
    border-bottom: 1rpx solid $surface-container;

    &:last-child {
      border-bottom: none;
      padding-bottom: 0;
    }

    .news-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 12rpx;

      .news-tag {
        align-self: flex-start;
        padding: 6rpx 16rpx;
        border-radius: 8rpx;
        font-size: 22rpx;
        font-weight: 500;

        &.tag-important {
          background: $error-container;
          color: $on-error-container;
        }
        &.tag-health {
          background: $secondary-fixed;
          color: $on-secondary-fixed-variant;
        }
      }
      .news-title {
        font-size: 28rpx;
        font-weight: 600;
        color: $on-surface;
        line-height: 1.5;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }
      .news-meta {
        display: flex;
        align-items: center;
        gap: 12rpx;
        font-size: 24rpx;
        color: $on-surface-variant;

        .dot {
          opacity: 0.5;
        }
      }
    }
    .news-image {
      width: 200rpx;
      height: 150rpx;
      border-radius: 16rpx;
      background: $surface-container;
      flex-shrink: 0;
    }
  }
}
</style>
