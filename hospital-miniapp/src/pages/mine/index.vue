<template>
  <view class="mine-page">
    <!-- 用户信息卡片 -->
    <view class="user-card" @click="goProfile">
      <view class="avatar-wrap">
        <image class="avatar" :src="userInfo?.avatarUrl || '/static/default-avatar.png'" mode="aspectFill" />
      </view>
      <view class="info">
        <text class="name">{{ userInfo?.nickName || '未登录' }}</text>
        <text class="phone">{{ userInfo?.phone || '点击登录账号' }}</text>
      </view>
      <view class="arrow-wrap">
        <view class="arrow"></view>
      </view>
    </view>

    <!-- 快捷功能 -->
    <view class="quick-menu">
      <view class="quick-item" @click="goMyOrder">
        <view class="icon-wrap" style="background: #e8f2ff;">
          <text class="icon">📅</text>
        </view>
        <text class="label">我的预约</text>
      </view>
      <view class="quick-item" @click="goMyVisit">
        <view class="icon-wrap" style="background: #e6f7ed;">
          <text class="icon">💊</text>
        </view>
        <text class="label">我的医嘱</text>
      </view>
      <view class="quick-item" @click="goMyExam">
        <view class="icon-wrap" style="background: #fff2e8;">
          <text class="icon">🔬</text>
        </view>
        <text class="label">检查报告</text>
      </view>
      <view class="quick-item" @click="goMyLab">
        <view class="icon-wrap" style="background: #fce8ec;">
          <text class="icon">🧪</text>
        </view>
        <text class="label">检验报告</text>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-group">
      <view class="menu-item" @click="goVisitUser">
        <view class="item-left">
          <view class="item-icon" style="background: #e8f2ff; color: #0052d9;">👤</view>
          <text class="label">就诊人管理</text>
        </view>
        <view class="arrow"></view>
      </view>
      <view class="menu-item" @click="goSuggest">
        <view class="item-left">
          <view class="item-icon" style="background: #e6f7ed; color: #006c46;">💬</view>
          <text class="label">意见反馈</text>
        </view>
        <view class="arrow"></view>
      </view>
      <view class="menu-item" @click="goPassword">
        <view class="item-left">
          <view class="item-icon" style="background: #fff2e8; color: #822600;">🔒</view>
          <text class="label">修改密码</text>
        </view>
        <view class="arrow"></view>
      </view>
    </view>

    <!-- 登录/退出按钮 -->
    <button v-if="isLoggedIn" class="btn-logout" @click="logout">退出登录</button>
    <button v-else class="btn-primary" @click="goLogin">去登录</button>
  </view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores/user'
import { getWxUserInfo } from '@/api/user'
import { goTo } from '@/utils/navigation'

const userStore = useUserStore()
const isLoggedIn = computed(() => userStore.isLoggedIn)
const userInfo = ref<any>(null)

async function loadUser() {
  if (!userStore.userId) return
  try {
    const res = await getWxUserInfo(userStore.userId)
    userInfo.value = res.data
  } catch (e) {
    console.error(e)
  }
}

function goLogin() {
  goTo('/pages/login/index')
}

function goProfile() {
  if (!isLoggedIn.value) return goLogin()
  goTo('/pages/profile/index')
}

function goVisitUser() {
  if (!isLoggedIn.value) return goLogin()
  goTo('/pages/visit-user/list')
}

function goMyOrder() {
  if (!isLoggedIn.value) return goLogin()
  goTo('/pages/my-appointment/list')
}

function goMyVisit() {
  if (!isLoggedIn.value) return goLogin()
  goTo('/pages/my-visit/list')
}

function goMyExam() {
  if (!isLoggedIn.value) return goLogin()
  goTo('/pages/my-exam/list')
}

function goMyLab() {
  if (!isLoggedIn.value) return goLogin()
  goTo('/pages/my-lab/list')
}

function goSuggest() {
  if (!isLoggedIn.value) return goLogin()
  goTo('/pages/suggest/index')
}

function goPassword() {
  if (!isLoggedIn.value) return goLogin()
  uni.showModal({
    title: '修改密码',
    editable: true,
    placeholderText: '请输入新密码',
    success: async (res: any) => {
      if (res.confirm && res.content) {
        uni.showToast({ title: '功能开发中', icon: 'none' })
      }
    }
  })
}

function logout() {
  uni.showModal({
    title: '确认退出',
    content: '确定要退出登录吗？',
    success: (res) => {
      if (res.confirm) {
        userStore.logout()
        userInfo.value = null
        uni.showToast({ title: '已退出' })
      }
    }
  })
}

onShow(() => loadUser())
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.mine-page {
  padding: 24rpx;
  background: $surface;
  min-height: 100vh;
}

/* 用户信息卡片 */
.user-card {
  display: flex;
  align-items: center;
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 40rpx;
  margin-bottom: 24rpx;
  box-shadow: $shadow-card;
  transition: all 0.2s;

  &:active {
    transform: scale(0.98);
  }

  .avatar-wrap {
    width: 120rpx;
    height: 120rpx;
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

  .info {
    flex: 1;
    margin-left: 28rpx;

    .name {
      display: block;
      font-size: 34rpx;
      font-weight: 700;
      color: $on-surface;
    }

    .phone {
      display: block;
      font-size: 26rpx;
      color: $on-surface-variant;
      margin-top: 8rpx;
    }
  }

  .arrow-wrap {
    width: 48rpx;
    height: 48rpx;
    border-radius: 50%;
    background: $surface-container;
    display: flex;
    align-items: center;
    justify-content: center;

    .arrow {
      width: 16rpx;
      height: 16rpx;
      border-top: 4rpx solid $outline;
      border-right: 4rpx solid $outline;
      transform: rotate(45deg);
    }
  }
}

/* 快捷功能 */
.quick-menu {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16rpx;
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 32rpx 24rpx;
  margin-bottom: 24rpx;
  box-shadow: $shadow-card;

  .quick-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12rpx;
    transition: all 0.2s;

    &:active {
      transform: scale(0.9);
    }

    .icon-wrap {
      width: 88rpx;
      height: 88rpx;
      border-radius: 24rpx;
      display: flex;
      align-items: center;
      justify-content: center;

      .icon {
        font-size: 40rpx;
      }
    }

    .label {
      font-size: 24rpx;
      color: $on-surface;
      font-weight: 500;
    }
  }
}

/* 菜单组 */
.menu-group {
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 8rpx 0;
  margin-bottom: 24rpx;
  box-shadow: $shadow-card;

  .menu-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 28rpx 32rpx;
    border-bottom: 1rpx solid $surface-container;
    transition: all 0.2s;

    &:last-child {
      border-bottom: none;
    }

    &:active {
      background: $surface-container-low;
    }

    .item-left {
      display: flex;
      align-items: center;
      gap: 20rpx;

      .item-icon {
        width: 56rpx;
        height: 56rpx;
        border-radius: 16rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 28rpx;
      }

      .label {
        font-size: 30rpx;
        color: $on-surface;
        font-weight: 500;
      }
    }

    .arrow {
      width: 16rpx;
      height: 16rpx;
      border-top: 4rpx solid $outline;
      border-right: 4rpx solid $outline;
      transform: rotate(45deg);
    }
  }
}

/* 退出登录按钮 */
.btn-logout {
  margin-top: 40rpx;
  height: 96rpx;
  line-height: 96rpx;
  background: $error-container;
  color: $on-error-container;
  border-radius: $radius-md;
  font-size: 32rpx;
  font-weight: 600;
  border: none;

  &:active {
    background: #ffcdd2;
  }
}

.btn-primary {
  margin-top: 40rpx;
}
</style>
