<template>
  <view class="list-page">
    <view v-for="item in list" :key="item.visitId" class="visit-card">
      <view class="card-header">
        <view class="visit-no">
          <text class="icon">📋</text>
          <text class="text">就诊号：{{ item.visitNo || item.visitId }}</text>
        </view>
        <text class="time">{{ item.visitTime }}</text>
      </view>
      <view class="card-body">
        <view class="info-item">
          <text class="label">诊断</text>
          <text class="value">{{ item.diagnosis || '暂无' }}</text>
        </view>
        <view class="info-item">
          <text class="label">医嘱</text>
          <text class="value">{{ item.advice || '暂无' }}</text>
        </view>
        <view class="info-item">
          <text class="label">处方</text>
          <text class="value">{{ item.prescription || '暂无' }}</text>
        </view>
      </view>
    </view>
    <view v-if="!list.length" class="empty">
      <text class="empty-icon">💊</text>
      <text class="empty-text">暂无医嘱记录</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores/user'
import { getVisitListByUser } from '@/api/visit'

const userStore = useUserStore()
const list = ref<any[]>([])

async function loadData() {
  if (!userStore.userId) return
  try {
    const res = await getVisitListByUser(userStore.userId)
    list.value = res.data || []
  } catch (e) {
    list.value = []
  }
}

onShow(() => loadData())
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.list-page {
  padding: 24rpx;
  background: $surface;
  min-height: 100vh;
}

.visit-card {
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: $shadow-card;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid $surface-container;

    .visit-no {
      display: flex;
      align-items: center;
      gap: 10rpx;

      .icon {
        font-size: 28rpx;
      }

      .text {
        font-size: 26rpx;
        color: $on-surface;
        font-weight: 600;
      }
    }

    .time {
      font-size: 24rpx;
      color: $on-surface-variant;
    }
  }

  .card-body {
    .info-item {
      margin-top: 16rpx;

      &:first-child {
        margin-top: 0;
      }

      .label {
        display: block;
        font-size: 24rpx;
        color: $on-surface-variant;
        margin-bottom: 6rpx;
      }

      .value {
        display: block;
        font-size: 28rpx;
        color: $on-surface;
        line-height: 1.5;
        font-weight: 500;
      }
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
