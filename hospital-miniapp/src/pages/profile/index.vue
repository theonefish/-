<template>
  <view class="profile-page">
    <view class="form-card">
      <view class="avatar-section">
        <view class="avatar-wrap">
          <image class="avatar" :src="form.avatarUrl || '/static/default-avatar.png'" mode="aspectFill" />
          <view class="avatar-mask">
            <text>📷</text>
          </view>
        </view>
        <text class="avatar-tip">点击更换头像</text>
      </view>

      <view class="input-group">
        <text class="label">昵称</text>
        <input v-model="form.nickName" placeholder="请输入昵称" class="input-field" />
      </view>
      <view class="input-group">
        <text class="label">手机号</text>
        <input v-model="form.phone" placeholder="请输入手机号" type="number" class="input-field" />
      </view>
      <view class="input-group">
        <text class="label">头像链接</text>
        <input v-model="form.avatarUrl" placeholder="头像URL" class="input-field" />
      </view>
      <button class="btn-primary" @click="save">保存</button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getWxUserInfo, updateWxUser } from '@/api/user'

const userStore = useUserStore()
const form = reactive<any>({ nickName: '', phone: '', avatarUrl: '' })

async function loadData() {
  if (!userStore.userId) return
  const res = await getWxUserInfo(userStore.userId)
  Object.assign(form, res.data)
}

async function save() {
  try {
    await updateWxUser(userStore.userId, form)
    uni.showToast({ title: '保存成功' })
    setTimeout(() => uni.navigateBack(), 800)
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => loadData())
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.profile-page {
  padding: 24rpx;
  background: $surface;
  min-height: 100vh;
}

.form-card {
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 40rpx 32rpx;
  box-shadow: $shadow-card;
}

/* 头像区域 */
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 40rpx;

  .avatar-wrap {
    position: relative;
    width: 160rpx;
    height: 160rpx;
    border-radius: 50%;
    overflow: hidden;
    border: 4rpx solid $surface-container-high;
    box-shadow: $shadow-card;

    .avatar {
      width: 100%;
      height: 100%;
    }

    .avatar-mask {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 50%;
      background: rgba(0, 0, 0, 0.4);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 32rpx;
    }
  }

  .avatar-tip {
    font-size: 24rpx;
    color: $on-surface-variant;
    margin-top: 12rpx;
  }
}

.input-group {
  margin-bottom: 28rpx;

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
</style>
