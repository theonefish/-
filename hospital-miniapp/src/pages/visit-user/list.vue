<template>
  <view class="list-page">
    <view v-for="item in list" :key="item.visitId" class="visit-card">
      <view class="card-left">
        <view class="avatar">
          <text>{{ item.name?.charAt(0) || '?' }}</text>
        </view>
        <view class="info">
          <view class="name-row">
            <text class="name">{{ item.name }}</text>
            <text v-if="item.isDefault === '1'" class="default-tag">默认</text>
          </view>
          <text class="sub">{{ item.cardNo }}</text>
          <text class="sub">{{ item.phone }} · {{ SEX_MAP[item.sex || 2] }} · {{ item.age }}岁</text>
        </view>
      </view>
      <view class="card-actions">
        <view class="action-btn edit" @click="goEdit(item)">
          <text>✏️</text>
        </view>
        <view class="action-btn delete" @click="handleDelete(item.visitId)">
          <text>🗑️</text>
        </view>
      </view>
    </view>

    <view v-if="!list.length" class="empty">
      <text class="empty-icon">👤</text>
      <text class="empty-text">暂无就诊人</text>
    </view>

    <button class="btn-add" @click="goEdit()">
      <text class="add-icon">+</text>
      添加就诊人
    </button>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores/user'
import { getVisitUserList, deleteVisitUser } from '@/api/visit-user'
import { SEX_MAP } from '@/utils/constant'
import { goTo } from '@/utils/navigation'

const userStore = useUserStore()
const list = ref<any[]>([])

async function loadData() {
  if (!userStore.userId) return
  const res = await getVisitUserList(userStore.userId)
  list.value = res.data || []
}

function goEdit(item?: any) {
  const url = item ? '/pages/visit-user/edit?data=' + encodeURIComponent(JSON.stringify(item)) : '/pages/visit-user/edit'
  goTo(url)
}

function handleDelete(id: number) {
  uni.showModal({
    title: '确认删除',
    content: '确定删除该就诊人吗？',
    success: async (res: any) => {
      if (res.confirm) {
        await deleteVisitUser(id)
        uni.showToast({ title: '删除成功' })
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

.visit-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 28rpx 32rpx;
  margin-bottom: 20rpx;
  box-shadow: $shadow-card;

  .card-left {
    display: flex;
    align-items: center;
    gap: 20rpx;
    flex: 1;
    min-width: 0;

    .avatar {
      width: 80rpx;
      height: 80rpx;
      border-radius: 50%;
      background: $primary;
      display: flex;
      align-items: center;
      justify-content: center;
      color: $on-primary;
      font-size: 32rpx;
      font-weight: 600;
      flex-shrink: 0;
    }

    .info {
      flex: 1;
      min-width: 0;

      .name-row {
        display: flex;
        align-items: center;
        gap: 12rpx;
        flex-wrap: wrap;

        .name {
          font-size: 30rpx;
          color: $on-surface;
          font-weight: 600;
        }

        .default-tag {
          font-size: 22rpx;
          color: $primary;
          background: $primary-fixed;
          padding: 4rpx 12rpx;
          border-radius: 8rpx;
          font-weight: 500;
        }
      }

      .sub {
        display: block;
        margin-top: 6rpx;
        font-size: 24rpx;
        color: $on-surface-variant;
      }
    }
  }

  .card-actions {
    display: flex;
    gap: 16rpx;

    .action-btn {
      width: 64rpx;
      height: 64rpx;
      border-radius: 16rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 28rpx;
      transition: all 0.2s;

      &.edit {
        background: $primary-fixed;

        &:active {
          background: $primary-fixed-dim;
        }
      }

      &.delete {
        background: $error-container;

        &:active {
          background: #ffcdd2;
        }
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

.btn-add {
  margin-top: 40rpx;
  height: 96rpx;
  line-height: 96rpx;
  background: $primary;
  color: $on-primary;
  border-radius: $radius-md;
  font-size: 32rpx;
  font-weight: 600;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  box-shadow: $shadow-float;

  &:active {
    transform: translateY(2rpx);
    box-shadow: $shadow-card;
  }

  .add-icon {
    font-size: 36rpx;
    font-weight: 400;
  }
}
</style>
