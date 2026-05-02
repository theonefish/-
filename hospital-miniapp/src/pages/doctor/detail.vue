<template>
  <view class="detail-page">
    <!-- 医生基本信息卡片 -->
    <view class="profile-card" v-if="doctor">
      <view class="avatar-wrap">
        <image class="avatar" :src="doctor.image || '/static/default-avatar.png'" mode="aspectFill" />
      </view>
      <view class="profile-info">
        <view class="name-row">
          <text class="name">{{ doctor.nickName || doctor.userName }}</text>
          <text class="title">{{ doctor.jobTitle || '医师' }}</text>
        </view>
        <text class="dept">{{ doctor.deptName || '科室' }}</text>
        <!-- 评分 -->
        <view class="rating-row" v-if="doctorStats.rating > 0">
          <view class="stars">
            <text v-for="n in 5" :key="n" class="star" :class="{ active: n <= Math.round(doctorStats.rating) }">⭐</text>
          </view>
          <text class="rating-score">{{ doctorStats.rating }}</text>
          <text class="rating-count">({{ doctorStats.ratingCount }}条评价)</text>
        </view>
      </view>
    </view>

    <!-- 医生统计数据 -->
    <view class="stats-bar" v-if="doctor">
      <view class="stat-item">
        <text class="stat-num">{{ doctorStats.consultCount }}</text>
        <text class="stat-label">问诊量</text>
      </view>
      <view class="stat-divider"></view>
      <view class="stat-item">
        <text class="stat-num">{{ doctorStats.goodRate }}%</text>
        <text class="stat-label">好评率</text>
      </view>
      <view class="stat-divider"></view>
      <view class="stat-item">
        <text class="stat-num">{{ doctorStats.years }}</text>
        <text class="stat-label">从业年限</text>
      </view>
      <view class="stat-divider"></view>
      <view class="stat-item">
        <text class="stat-num">{{ doctorStats.patientCount }}</text>
        <text class="stat-label">服务患者</text>
      </view>
    </view>

    <!-- 擅长方向 - 标签化展示 -->
    <view class="info-section" v-if="doctor">
      <view class="section-header">
        <text class="section-icon">💡</text>
        <text class="section-title">擅长方向</text>
      </view>
      <view class="section-body">
        <!-- 如果有擅长内容，用标签形式展示 -->
        <view v-if="specialtyTags.length > 0" class="tag-list">
          <text v-for="(tag, idx) in specialtyTags" :key="idx" class="specialty-tag">{{ tag }}</text>
        </view>
        <!-- 否则显示暂无 -->
        <text v-else class="content">暂无</text>
        <!-- 详细描述 -->
        <text v-if="doctor.specialty" class="specialty-desc">{{ doctor.specialty }}</text>
      </view>
    </view>

    <!-- 个人简介 -->
    <view class="info-section" v-if="doctor">
      <view class="section-header">
        <text class="section-icon">📝</text>
        <text class="section-title">个人简介</text>
      </view>
      <view class="section-body">
        <text class="content">{{ doctor.introduction || '暂无' }}</text>
      </view>
    </view>

    <!-- 医生执业信息 -->
    <view class="info-section" v-if="doctor">
      <view class="section-header">
        <text class="section-icon">🏥</text>
        <text class="section-title">执业信息</text>
      </view>
      <view class="section-body">
        <view class="info-grid">
          <view class="info-item">
            <text class="info-label">执业医院</text>
            <text class="info-value">康华医院</text>
          </view>
          <view class="info-item">
            <text class="info-label">所属科室</text>
            <text class="info-value">{{ doctor.deptName || '-' }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">职称</text>
            <text class="info-value">{{ doctor.jobTitle || '医师' }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">执业编号</text>
            <text class="info-value">{{ doctor.licenseNo || '110000000000' }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 出诊排班 -->
    <view class="schedule-section">
      <view class="section-header">
        <text class="section-icon">📅</text>
        <text class="section-title">出诊排班</text>
      </view>
      <view v-if="!scheduleList.length" class="empty">
        <text class="empty-icon">📭</text>
        <text class="empty-text">暂无排班</text>
      </view>
      <view v-for="item in scheduleList" :key="item.scheduleId" class="schedule-card">
        <view class="schedule-left">
          <text class="date">{{ item.times }}</text>
          <text class="period">{{ item.timeType }}</text>
        </view>
        <view class="schedule-center">
          <text class="price">¥{{ item.price }}</text>
          <text class="remain" :class="{ urgent: item.lastAmount <= 5 }">剩余 {{ item.lastAmount }} 号</text>
        </view>
        <button
          class="btn-book"
          :class="{ disabled: item.lastAmount <= 0 }"
          :disabled="item.lastAmount <= 0"
          @click="goAppointment(item)"
        >
          {{ item.lastAmount > 0 ? '预约' : '约满' }}
        </button>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getDoctorDetail, getDoctorList } from '@/api/doctor'
import { getScheduleList } from '@/api/schedule'
import { useUserStore } from '@/stores/user'
import { goTo } from '@/utils/navigation'

const doctor = ref<any>(null)
const scheduleList = ref<any[]>([])
let doctorId = 0
const userStore = useUserStore()

// 医生统计数据（模拟数据，实际应从接口获取）
const doctorStats = ref({
  rating: 4.8,
  ratingCount: 128,
  consultCount: 2560,
  goodRate: 98,
  years: 15,
  patientCount: 3200
})

// 将擅长方向拆分为标签
const specialtyTags = computed(() => {
  if (!doctor.value?.specialty) return []
  // 按常见分隔符拆分：逗号、顿号、空格
  const tags = doctor.value.specialty
    .split(/[,，、\s]+/)
    .map((t: string) => t.trim())
    .filter((t: string) => t.length > 0 && t.length <= 10)
  // 最多显示8个标签
  return tags.slice(0, 8)
})

function checkLogin() {
  if (!userStore.isLoggedIn) {
    uni.showModal({
      title: '提示',
      content: '预约需要登录后才能进行',
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

// 获取医生ID - 兼容不同字段名
function getDoctorId(d: any): number {
  return d.userId || d.doctorId || d.id || 0
}

async function loadDetail() {
  try {
    const res = await getDoctorDetail(doctorId).catch(async () => {
      const listRes = await getDoctorList({ page: 1, pageSize: 100 })
      return { data: listRes.data.list.find((d: any) => getDoctorId(d) === doctorId) }
    })
    doctor.value = res.data
    console.log('医生详情加载成功:', doctor.value)
  } catch (e) {
    console.error('加载医生详情失败:', e)
    uni.showToast({ title: '加载医生信息失败', icon: 'none' })
  }
}

async function loadSchedule() {
  const today = new Date().toISOString().split('T')[0]
  const end = new Date(Date.now() + 7 * 86400000).toISOString().split('T')[0]
  const res = await getScheduleList({ doctorId, startDate: today, endDate: end, page: 1, pageSize: 30 })
  scheduleList.value = res.data.list || []
}

function goAppointment(item: any) {
  if (!checkLogin()) return
  const data = encodeURIComponent(JSON.stringify({
    doctorId,
    scheduleId: item.scheduleId,
    doctorName: doctor.value?.nickName || doctor.value?.userName,
    deptName: doctor.value?.deptName,
    date: item.times,
    timeType: item.timeType,
    price: item.price
  }))
  goTo('/pages/appointment/confirm?data=' + data)
}

onLoad((opt: any) => {
  doctorId = Number(opt.id)
  loadDetail()
  loadSchedule()
})
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.detail-page {
  padding: 24rpx;
  background: $surface;
  min-height: 100vh;
}

/* 医生信息卡片 */
.profile-card {
  display: flex;
  align-items: center;
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 40rpx;
  margin-bottom: 24rpx;
  box-shadow: $shadow-card;

  .avatar-wrap {
    width: 140rpx;
    height: 140rpx;
    border-radius: 50%;
    overflow: hidden;
    border: 3rpx solid $surface-container-high;
    box-shadow: $shadow-card;
    flex-shrink: 0;

    .avatar {
      width: 100%;
      height: 100%;
    }
  }

  .profile-info {
    margin-left: 32rpx;
    flex: 1;

    .name-row {
      display: flex;
      align-items: center;
      gap: 16rpx;
      flex-wrap: wrap;

      .name {
        font-size: 36rpx;
        font-weight: 700;
        color: $on-surface;
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
      font-size: 28rpx;
      color: $on-surface-variant;
      margin-top: 12rpx;
    }

    /* 评分行 */
    .rating-row {
      display: flex;
      align-items: center;
      gap: 12rpx;
      margin-top: 16rpx;

      .stars {
        display: flex;
        gap: 4rpx;

        .star {
          font-size: 24rpx;
          opacity: 0.3;
          transition: opacity 0.2s;

          &.active {
            opacity: 1;
          }
        }
      }

      .rating-score {
        font-size: 28rpx;
        color: $tertiary;
        font-weight: 700;
      }

      .rating-count {
        font-size: 24rpx;
        color: $on-surface-variant;
      }
    }
  }
}

/* 统计数据栏 */
.stats-bar {
  display: flex;
  align-items: center;
  justify-content: space-around;
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 32rpx 24rpx;
  margin-bottom: 24rpx;
  box-shadow: $shadow-card;

  .stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8rpx;
    flex: 1;

    .stat-num {
      font-size: 36rpx;
      font-weight: 700;
      color: $primary;
    }

    .stat-label {
      font-size: 24rpx;
      color: $on-surface-variant;
    }
  }

  .stat-divider {
    width: 1rpx;
    height: 60rpx;
    background: $surface-container;
  }
}

/* 信息区块 */
.info-section {
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: $shadow-card;

  .section-header {
    display: flex;
    align-items: center;
    gap: 12rpx;
    margin-bottom: 20rpx;

    .section-icon {
      font-size: 32rpx;
    }

    .section-title {
      font-size: 30rpx;
      font-weight: 700;
      color: $on-surface;
    }
  }

  .section-body {
    /* 擅长标签列表 */
    .tag-list {
      display: flex;
      flex-wrap: wrap;
      gap: 16rpx;
      margin-bottom: 20rpx;

      .specialty-tag {
        padding: 10rpx 24rpx;
        background: $primary-fixed;
        color: $on-primary-fixed-variant;
        border-radius: 32rpx;
        font-size: 26rpx;
        font-weight: 500;
      }
    }

    .specialty-desc {
      display: block;
      font-size: 28rpx;
      color: $on-surface-variant;
      line-height: 1.7;
      margin-top: 16rpx;
      padding-top: 16rpx;
      border-top: 1rpx solid $surface-container;
    }

    .content {
      font-size: 28rpx;
      color: $on-surface-variant;
      line-height: 1.7;
    }

    /* 执业信息网格 */
    .info-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 20rpx;

      .info-item {
        display: flex;
        flex-direction: column;
        gap: 8rpx;
        padding: 16rpx;
        background: $surface-container-low;
        border-radius: 16rpx;

        .info-label {
          font-size: 24rpx;
          color: $on-surface-variant;
        }

        .info-value {
          font-size: 28rpx;
          color: $on-surface;
          font-weight: 600;
        }
      }
    }
  }
}

/* 排班区块 */
.schedule-section {
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 32rpx;
  box-shadow: $shadow-card;

  .section-header {
    display: flex;
    align-items: center;
    gap: 12rpx;
    margin-bottom: 24rpx;

    .section-icon {
      font-size: 32rpx;
    }

    .section-title {
      font-size: 30rpx;
      font-weight: 700;
      color: $on-surface;
    }
  }
}

/* 排班卡片 */
.schedule-card {
  display: flex;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid $surface-container;

  &:last-child {
    border: none;
    padding-bottom: 0;
  }

  .schedule-left {
    width: 200rpx;
    flex-shrink: 0;

    .date {
      display: block;
      font-size: 28rpx;
      color: $on-surface;
      font-weight: 600;
    }

    .period {
      display: block;
      font-size: 24rpx;
      color: $on-surface-variant;
      margin-top: 8rpx;
    }
  }

  .schedule-center {
    flex: 1;

    .price {
      display: block;
      font-size: 32rpx;
      color: $tertiary;
      font-weight: 700;
    }

    .remain {
      display: block;
      font-size: 24rpx;
      color: $on-surface-variant;
      margin-top: 6rpx;

      &.urgent {
        color: $error;
        font-weight: 500;
      }
    }
  }

  .btn-book {
    width: 130rpx;
    height: 64rpx;
    line-height: 64rpx;
    background: $primary;
    color: $on-primary;
    border-radius: 32rpx;
    font-size: 26rpx;
    font-weight: 600;
    padding: 0;
    border: none;
    box-shadow: $shadow-float;

    &:active {
      transform: translateY(2rpx);
      box-shadow: $shadow-card;
    }

    &.disabled {
      background: $surface-dim;
      color: $on-surface-variant;
      box-shadow: none;
    }
  }
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80rpx 40rpx;

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
