<template>
  <view class="login-page">
    <!-- 顶部品牌区域 -->
    <view class="brand-header">
      <view class="brand-icon">🏥</view>
      <text class="title">康华医院</text>
      <text class="sub">预约挂号系统</text>
    </view>

    <!-- 登录表单 -->
    <view class="form-card">
      <view class="input-group">
        <text class="label">账号</text>
        <input v-model="form.username" placeholder="请输入账号" class="input-field" />
      </view>
      <view class="input-group">
        <text class="label">密码</text>
        <input v-model="form.password" placeholder="请输入密码" password class="input-field" />
      </view>
      <button class="btn-primary" @click="handleLogin">登录</button>
      <button class="btn-wx" @click="handleWxLogin">
        <text class="wx-icon">💬</text>
        微信一键登录
      </button>
      <view class="actions">
        <text @click="goRegister">还没有账号？去注册</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { login, wxLogin } from '@/api/auth'
import { useUserStore } from '@/stores/user'
import { goTo } from '@/utils/navigation'

const userStore = useUserStore()
const form = reactive({ username: '', password: '' })

async function handleLogin() {
  if (!form.username || !form.password) {
    uni.showToast({ title: '请填写完整', icon: 'none' })
    return
  }
  try {
    const res: any = await login(form)
    userStore.setLogin({ token: res.data.token, userId: Number(res.data.userId || 0) })
    uni.showToast({ title: '登录成功' })
    setTimeout(() => uni.switchTab({ url: '/pages/index/index' }), 800)
  } catch (e: any) {
    console.error(e)
    uni.showToast({ title: e.message || '登录失败', icon: 'none' })
  }
}

async function handleWxLogin() {
  uni.login({
    provider: 'weixin',
    success: async (loginRes: any) => {
      try {
        const res: any = await wxLogin({ openId: loginRes.code })
        userStore.setLogin({ token: res.data.token, userId: Number(res.data.userId || 0) })
        uni.showToast({ title: '登录成功' })
        setTimeout(() => uni.switchTab({ url: '/pages/index/index' }), 800)
      } catch (e: any) {
        if (e.message === '用户未注册') {
          uni.showToast({ title: '请先注册', icon: 'none' })
          setTimeout(() => goTo('/pages/register/index'), 800)
        } else {
          uni.showToast({ title: e.message || '登录失败', icon: 'none' })
        }
      }
    },
    fail: () => {
      uni.showToast({ title: '微信登录失败', icon: 'none' })
    }
  })
}

function goRegister() {
  goTo('/pages/register/index')
}

onLoad(() => {
  if (userStore.isLoggedIn) {
    uni.switchTab({ url: '/pages/index/index' })
  }
})
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.login-page {
  padding: 80rpx 40rpx;
  background: $surface;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 品牌头部 */
.brand-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 60rpx;

  .brand-icon {
    width: 120rpx;
    height: 120rpx;
    border-radius: 32rpx;
    background: $primary;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 60rpx;
    margin-bottom: 24rpx;
    box-shadow: $shadow-float;
  }

  .title {
    display: block;
    font-size: 44rpx;
    font-weight: 700;
    color: $on-surface;
  }

  .sub {
    display: block;
    margin-top: 12rpx;
    font-size: 28rpx;
    color: $on-surface-variant;
  }
}

/* 表单卡片 */
.form-card {
  background: $surface-container-lowest;
  border-radius: 32rpx;
  padding: 48rpx 40rpx;
  box-shadow: $shadow-card;
}

.input-group {
  margin-bottom: 32rpx;

  .label {
    display: block;
    margin-bottom: 12rpx;
    font-size: 28rpx;
    color: $on-surface;
    font-weight: 500;
  }

  .input-field {
    width: 100%;
    height: 96rpx;
    padding: 0 24rpx;
    background: $surface-container-low;
    border: 2rpx solid $outline-variant;
    border-radius: $radius-md;
    font-size: 28rpx;
    color: $on-surface;
    box-sizing: border-box;
    transition: border-color 0.2s;

    &:focus {
      border-color: $primary;
      background: $surface-container-lowest;
    }

    &::placeholder {
      color: $on-surface-variant;
    }
  }
}

.btn-primary {
  margin-top: 20rpx;
  width: 100%;
}

.btn-wx {
  margin-top: 24rpx;
  height: 96rpx;
  line-height: 96rpx;
  background: $secondary;
  color: $on-secondary;
  border-radius: $radius-md;
  font-size: 32rpx;
  font-weight: 600;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 108, 70, 0.2);

  &:active {
    transform: translateY(2rpx);
    box-shadow: none;
  }

  .wx-icon {
    font-size: 36rpx;
  }
}

.actions {
  margin-top: 32rpx;
  text-align: center;
  font-size: 26rpx;
  color: $primary;
  font-weight: 500;
}
</style>
