<template>
  <view class="list-page">
    <view v-for="item in list" :key="item.makeId" class="order-card">
      <view class="card-header">
        <view class="order-no">
          <text class="label">订单号</text>
          <text class="value">{{ item.orderNo || item.makeId }}</text>
        </view>
        <text class="status" :style="{ color: ORDER_STATUS_COLOR[item.status || 0] }">
          {{ ORDER_STATUS[item.status || 0] }}
        </text>
      </view>
      <view class="card-body">
        <view class="info-row">
          <text class="icon">👨‍⚕️</text>
          <text class="text">{{ item.doctorName || '医生' }}</text>
        </view>
        <view class="info-row">
          <text class="icon">📅</text>
          <text class="text">{{ item.createTime }}</text>
        </view>
      </view>
      <view v-if="(item.status || 0) === 0" class="card-footer">
        <button class="btn-cancel" @click="cancel(item.makeId)">取消预约</button>
      </view>
    </view>
    <view v-if="!list.length" class="empty">
      <text class="empty-icon">📅</text>
      <text class="empty-text">暂无预约</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores/user'
import { getUserOrders, cancelOrder } from '@/api/order'
import { ORDER_STATUS, ORDER_STATUS_COLOR } from '@/utils/constant'

const userStore = useUserStore()
const list = ref<any[]>([])

async function loadData() {
  if (!userStore.userId) return
  const res = await getUserOrders(userStore.userId)
  list.value = res.data || []
}

function cancel(id: number) {
  uni.showModal({
    title: '确认取消',
    content: '确定取消该预约吗？',
    success: async (res: any) => {
      if (res.confirm) {
        await cancelOrder(id)
        uni.showToast({ title: '已取消' })
        loadData()
      }
    }
  })
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

.order-card {
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: $shadow-card;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid $surface-container;

    .order-no {
      .label {
        font-size: 22rpx;
        color: $on-surface-variant;
        margin-right: 8rpx;
      }

      .value {
        font-size: 26rpx;
        color: $on-surface;
        font-weight: 500;
      }
    }

    .status {
      font-size: 26rpx;
      font-weight: 600;
      background: $surface-container-low;
      padding: 6rpx 16rpx;
      border-radius: 8rpx;
    }
  }

  .card-body {
    .info-row {
      display: flex;
      align-items: center;
      gap: 12rpx;
      margin-top: 12rpx;

      &:first-child {
        margin-top: 0;
      }

      .icon {
        font-size: 28rpx;
      }

      .text {
        font-size: 28rpx;
        color: $on-surface;
      }
    }
  }

  .card-footer {
    margin-top: 24rpx;
    display: flex;
    justify-content: flex-end;

    .btn-cancel {
      width: 180rpx;
      height: 60rpx;
      line-height: 60rpx;
      background: $surface-container-lowest;
      color: $error;
      border: 2rpx solid $error;
      border-radius: 30rpx;
      font-size: 26rpx;
      font-weight: 500;
      padding: 0;

      &:active {
        background: $error-container;
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
