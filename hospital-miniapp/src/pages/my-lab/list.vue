<template>
  <view class="list-page">
    <view v-for="item in list" :key="item.inspectrepId" class="report-card">
      <view class="card-header">
        <text class="name">{{ item.inspectname }}</text>
        <text class="price">¥{{ item.inspectprice || 0 }}</text>
      </view>
      <view class="card-body">
        <view class="info-row">
          <text class="label">检验部位</text>
          <text class="value">{{ item.inspectpart || '-' }}</text>
        </view>
        <view class="info-row">
          <text class="label">报告内容</text>
          <text class="value">{{ item.inspectreport || '暂无报告' }}</text>
        </view>
      </view>
      <view class="card-footer">
        <text class="time">{{ item.inspecttime || item.createTime }}</text>
      </view>
    </view>
    <view v-if="!list.length" class="empty">
      <text class="empty-icon">🧪</text>
      <text class="empty-text">暂无检验报告</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores/user'
import { getLabListByUser } from '@/api/lab'

const userStore = useUserStore()
const list = ref<any[]>([])

async function loadData() {
  if (!userStore.userId) return
  try {
    const res = await getLabListByUser(userStore.userId)
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

.report-card {
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: $shadow-card;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid $surface-container;

    .name {
      font-size: 30rpx;
      color: $on-surface;
      font-weight: 700;
    }

    .price {
      font-size: 32rpx;
      color: $tertiary;
      font-weight: 700;
    }
  }

  .card-body {
    .info-row {
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

  .card-footer {
    margin-top: 20rpx;
    padding-top: 16rpx;
    border-top: 1rpx solid $surface-container;

    .time {
      font-size: 24rpx;
      color: $on-surface-variant;
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
