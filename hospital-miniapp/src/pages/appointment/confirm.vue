<template>
  <view class="confirm-page">
    <!-- 预约信息卡片 -->
    <view class="info-card">
      <view class="info-header">
        <text class="info-icon">📋</text>
        <text class="info-title">预约信息</text>
      </view>
      <view class="info-row">
        <text class="label">预约医生</text>
        <text class="value">{{ data.doctorName }} {{ data.deptName }}</text>
      </view>
      <view class="info-row">
        <text class="label">预约时间</text>
        <text class="value">{{ data.date }} {{ data.timeType }}</text>
      </view>
      <view class="info-row">
        <text class="label">挂号费用</text>
        <text class="value price">¥{{ data.price }}</text>
      </view>
    </view>

    <!-- 选择就诊人 -->
    <view class="section-header">
      <text class="section-icon">👤</text>
      <text class="section-title">选择就诊人</text>
    </view>

    <view v-if="!visitUsers.length" class="empty-tip" @click="goAddVisitUser">
      <text class="empty-icon">➕</text>
      <text>暂无就诊人，点击添加</text>
    </view>

    <view v-for="item in visitUsers" :key="item.visitId" class="visit-card"
      :class="{ active: selectedVisitId === item.visitId }" @click="selectedVisitId = item.visitId">
      <view class="visit-left">
        <view class="avatar-circle">
          <text>{{ item.name?.charAt(0) || '?' }}</text>
        </view>
        <view class="visit-info">
          <text class="name">{{ item.name }}</text>
          <text class="sub">{{ item.cardNo }} {{ item.phone }}</text>
        </view>
      </view>
      <view class="visit-right">
        <text v-if="item.isDefault === '1'" class="default-tag">默认</text>
        <view class="radio" :class="{ checked: selectedVisitId === item.visitId }">
          <text v-if="selectedVisitId === item.visitId">✓</text>
        </view>
      </view>
    </view>

    <button class="btn-submit" :disabled="!selectedVisitId" @click="submit">确认预约</button>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores/user'
import { getVisitUserList } from '@/api/visit-user'
import { createOrder } from '@/api/order'
import { goTo } from '@/utils/navigation'

const userStore = useUserStore()
const data = ref<any>({})
const visitUsers = ref<any[]>([])
const selectedVisitId = ref<number | null>(null)

async function loadVisitUsers() {
  if (!userStore.userId) {
    uni.showToast({ title: '请先登录', icon: 'none' })
    return
  }
  const res = await getVisitUserList(userStore.userId)
  visitUsers.value = res.data || []
  const def = visitUsers.value.find(v => v.isDefault === '1')
  if (def) selectedVisitId.value = def.visitId
}

function goAddVisitUser() {
  goTo('/pages/visit-user/edit')
}

async function submit() {
  if (!selectedVisitId.value) {
    uni.showToast({ title: '请选择就诊人', icon: 'none' })
    return
  }
  try {
    await createOrder({
      scheduleId: data.value.scheduleId,
      userId: userStore.userId,
      visitUserId: selectedVisitId.value,
      status: 0,
      payStatus: 0
    })
    uni.showToast({ title: '预约成功' })
    setTimeout(() => {
      uni.navigateBack({ delta: 1 })
    }, 800)
  } catch (e) {
    console.error(e)
  }
}

onLoad((opt: any) => {
  if (opt.data) {
    data.value = JSON.parse(decodeURIComponent(opt.data))
  }
})
onMounted(() => loadVisitUsers())
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.confirm-page {
  padding: 24rpx;
  background: $surface;
  min-height: 100vh;
}

/* 预约信息卡片 */
.info-card {
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 32rpx;
  box-shadow: $shadow-card;

  .info-header {
    display: flex;
    align-items: center;
    gap: 12rpx;
    margin-bottom: 24rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid $surface-container;

    .info-icon {
      font-size: 32rpx;
    }

    .info-title {
      font-size: 30rpx;
      font-weight: 700;
      color: $on-surface;
    }
  }

  .info-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16rpx 0;

    .label {
      font-size: 28rpx;
      color: $on-surface-variant;
    }

    .value {
      font-size: 28rpx;
      color: $on-surface;
      font-weight: 500;

      &.price {
        color: $tertiary;
        font-size: 32rpx;
        font-weight: 700;
      }
    }
  }
}

/* 区块标题 */
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

/* 空状态提示 */
.empty-tip {
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 48rpx;
  text-align: center;
  color: $primary;
  font-size: 28rpx;
  font-weight: 500;
  box-shadow: $shadow-card;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12rpx;

  .empty-icon {
    font-size: 48rpx;
  }
}

/* 就诊人卡片 */
.visit-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 28rpx 32rpx;
  margin-bottom: 20rpx;
  box-shadow: $shadow-card;
  border: 3rpx solid transparent;
  transition: all 0.2s;

  &.active {
    border-color: $primary;
  }

  &:active {
    transform: scale(0.98);
  }

  .visit-left {
    display: flex;
    align-items: center;
    gap: 20rpx;

    .avatar-circle {
      width: 72rpx;
      height: 72rpx;
      border-radius: 50%;
      background: $primary;
      display: flex;
      align-items: center;
      justify-content: center;
      color: $on-primary;
      font-size: 28rpx;
      font-weight: 600;
      flex-shrink: 0;
    }

    .visit-info {
      .name {
        display: block;
        font-size: 30rpx;
        color: $on-surface;
        font-weight: 600;
      }

      .sub {
        display: block;
        margin-top: 6rpx;
        font-size: 24rpx;
        color: $on-surface-variant;
      }
    }
  }

  .visit-right {
    display: flex;
    align-items: center;
    gap: 16rpx;

    .default-tag {
      font-size: 22rpx;
      color: $primary;
      background: $primary-fixed;
      padding: 4rpx 12rpx;
      border-radius: 8rpx;
      font-weight: 500;
    }

    .radio {
      width: 40rpx;
      height: 40rpx;
      border-radius: 50%;
      border: 3rpx solid $outline-variant;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s;

      &.checked {
        background: $primary;
        border-color: $primary;
        color: $on-primary;
        font-size: 22rpx;
        font-weight: 700;
      }
    }
  }
}

.btn-submit {
  margin-top: 40rpx;
  width: 100%;
}
</style>
