<template>
  <view class="detail-page">
    <view class="article-header">
      <text class="title">{{ item.title }}</text>
      <view class="meta">
        <text class="source">{{ item.source || '康华医院' }}</text>
        <text class="dot">·</text>
        <text class="time">{{ item.createTime }}</text>
      </view>
    </view>
    <rich-text class="content" :nodes="item.content"></rich-text>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getNewsList } from '@/api/news'

const item = ref<any>({})

onLoad(async (opt: any) => {
  // 方式一：直接传 JSON 对象（资讯列表页、首页都用这种方式）
  if (opt.item) {
    item.value = JSON.parse(decodeURIComponent(opt.item))
    return
  }
  // 方式二：传 ID，从接口获取详情（兜底方案）
  if (opt.id) {
    try {
      const res = await getNewsList({ page: 1, pageSize: 100 })
      const found = (res.data.list || []).find((n: any) => String(n.id || n.newsId) === String(opt.id))
      if (found) {
        item.value = found
      } else {
        uni.showToast({ title: '资讯不存在', icon: 'none' })
      }
    } catch (e) {
      console.error('加载资讯详情失败:', e)
    }
  }
})
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.detail-page {
  padding: 40rpx 32rpx;
  background: $surface-container-lowest;
  min-height: 100vh;
}

.article-header {
  margin-bottom: 40rpx;
  padding-bottom: 32rpx;
  border-bottom: 1rpx solid $surface-container;

  .title {
    display: block;
    font-size: 40rpx;
    font-weight: 700;
    color: $on-surface;
    line-height: 1.4;
  }

  .meta {
    display: flex;
    align-items: center;
    gap: 12rpx;
    margin-top: 20rpx;

    .source {
      font-size: 26rpx;
      color: $primary;
      background: $primary-fixed;
      padding: 4rpx 16rpx;
      border-radius: 8rpx;
      font-weight: 500;
    }

    .dot {
      font-size: 26rpx;
      color: $outline;
    }

    .time {
      font-size: 26rpx;
      color: $on-surface-variant;
    }
  }
}

.content {
  display: block;
  font-size: 30rpx;
  color: $on-surface-variant;
  line-height: 1.8;

  /* 富文本样式覆盖 */
  ::v-deep img,
  ::v-deep image {
    max-width: 100%;
    border-radius: 16rpx;
    margin: 24rpx 0;
  }

  ::v-deep p {
    margin: 20rpx 0;
  }

  ::v-deep h1,
  ::v-deep h2,
  ::v-deep h3 {
    color: $on-surface;
    margin: 32rpx 0 16rpx;
  }
}
</style>
