<template>
  <view class="register-page">
    <!-- 顶部品牌区域 -->
    <view class="brand-header">
      <view class="brand-icon">🏥</view>
      <text class="title">注册账号</text>
      <text class="sub">加入康华医院预约挂号</text>
    </view>

    <!-- 注册表单 -->
    <view class="form-card">
      <view class="input-group">
        <text class="label">账号</text>
        <input v-model="form.username" placeholder="请输入账号" class="input-field" />
      </view>
      <view class="input-group">
        <text class="label">密码</text>
        <input v-model="form.password" placeholder="请输入密码" password class="input-field" />
      </view>
      <view class="input-group">
        <text class="label">确认密码</text>
        <input v-model="form.confirmPassword" placeholder="请再次输入密码" password class="input-field" />
      </view>
      <view class="input-group">
        <text class="label">昵称</text>
        <input v-model="form.nickName" placeholder="请输入昵称" class="input-field" />
      </view>
      <view class="input-group">
        <text class="label">手机号</text>
        <input v-model="form.phone" placeholder="请输入手机号" type="number" class="input-field" />
      </view>
      <button class="btn-primary" @click="handleRegister">注册</button>
      <view class="actions">
        <text @click="goLogin">已有账号？去登录</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { wxRegister } from '@/api/auth'

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickName: '',
  phone: ''
})

async function handleRegister() {
  if (!form.username || !form.password || !form.confirmPassword) {
    uni.showToast({ title: '请填写完整', icon: 'none' })
    return
  }
  if (form.password !== form.confirmPassword) {
    uni.showToast({ title: '两次密码不一致', icon: 'none' })
    return
  }
  try {
    await wxRegister({
      userName: form.username,
      password: form.password,
      nickName: form.nickName,
      phone: form.phone
    })
    uni.showToast({ title: '注册成功' })
    setTimeout(() => uni.navigateBack(), 800)
  } catch (e) {
    console.error(e)
  }
}

function goLogin() {
  uni.navigateBack()
}
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.register-page {
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

.actions {
  margin-top: 32rpx;
  text-align: center;
  font-size: 26rpx;
  color: $primary;
  font-weight: 500;
}
</style>
