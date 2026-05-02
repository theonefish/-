<template>
  <view class="suggest-page">
    <view class="form-card">
      <view class="form-header">
        <text class="header-icon">💬</text>
        <text class="header-title">意见反馈</text>
        <text class="header-desc">您的建议是我们进步的动力</text>
      </view>
      <view class="textarea-wrap">
        <textarea v-model="content" placeholder="请输入您的意见或建议，我们会认真阅读每一条反馈..." maxlength="500" />
        <text class="count">{{ content.length }}/500</text>
      </view>
      <button class="btn-primary" @click="submit">提交反馈</button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { submitSuggest } from '@/api/suggest'

const userStore = useUserStore()
const content = ref('')

async function submit() {
  if (!content.value.trim()) {
    uni.showToast({ title: '请输入内容', icon: 'none' })
    return
  }
  try {
    await submitSuggest({ userId: userStore.userId, content: content.value })
    uni.showToast({ title: '提交成功' })
    content.value = ''
  } catch (e) {
    console.error(e)
  }
}
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.suggest-page {
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

.form-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 32rpx;

  .header-icon {
    font-size: 56rpx;
    margin-bottom: 12rpx;
  }

  .header-title {
    font-size: 32rpx;
    font-weight: 700;
    color: $on-surface;
  }

  .header-desc {
    font-size: 26rpx;
    color: $on-surface-variant;
    margin-top: 8rpx;
  }
}

.textarea-wrap {
  position: relative;
  background: $surface-container-low;
  border: 2rpx solid $outline-variant;
  border-radius: $radius-md;
  padding: 24rpx;
  margin-bottom: 32rpx;
  transition: border-color 0.2s;

  &:focus-within {
    border-color: $primary;
    background: $surface-container-lowest;
  }

  textarea {
    width: 100%;
    height: 300rpx;
    font-size: 28rpx;
    line-height: 1.6;
    color: $on-surface;

    &::placeholder {
      color: $on-surface-variant;
    }
  }

  .count {
    position: absolute;
    right: 24rpx;
    bottom: 24rpx;
    font-size: 24rpx;
    color: $on-surface-variant;
  }
}

.btn-primary {
  width: 100%;
}
</style>
