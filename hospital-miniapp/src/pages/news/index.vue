<template>
  <view class="news-page">
    <view v-for="item in newsList" :key="item.id" class="news-card" @click="goDetail(item)">
      <image class="cover" :src="item.cover || '/static/default-news.png'" mode="aspectFill" />
      <view class="info">
        <text class="title">{{ item.title }}</text>
        <view class="meta">
          <text class="source">{{ item.source || '康华医院' }}</text>
          <text class="time">{{ item.createTime }}</text>
        </view>
      </view>
    </view>
    <view v-if="!newsList.length" class="empty">
      <text class="empty-icon">📰</text>
      <text class="empty-text">暂无资讯</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getNewsList } from '@/api/news'
import { goTo } from '@/utils/navigation'

const newsList = ref<any[]>([])

async function loadData() {
  const res = await getNewsList({ page: 1, pageSize: 20 })
  newsList.value = res.data.list || []
}

function goDetail(item: any) {
  goTo('/pages/news/detail?item=' + encodeURIComponent(JSON.stringify(item)))
}

onMounted(() => loadData())
onShow(() => loadData())
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.news-page {
  padding: 24rpx;
  background: $surface;
  min-height: 100vh;
}

.news-card {
  display: flex;
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
  box-shadow: $shadow-card;
  transition: all 0.2s;

  &:active {
    transform: scale(0.98);
    box-shadow: $shadow-elevated;
  }

  .cover {
    width: 200rpx;
    height: 150rpx;
    border-radius: 16rpx;
    background: $surface-container;
    flex-shrink: 0;
  }

  .info {
    flex: 1;
    margin-left: 24rpx;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    min-width: 0;

    .title {
      font-size: 30rpx;
      color: $on-surface;
      font-weight: 600;
      line-height: 1.4;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .meta {
      display: flex;
      align-items: center;
      gap: 16rpx;
      margin-top: 12rpx;

      .source {
        font-size: 24rpx;
        color: $primary;
        background: $primary-fixed;
        padding: 4rpx 12rpx;
        border-radius: 8rpx;
        font-weight: 500;
      }

      .time {
        font-size: 24rpx;
        color: $on-surface-variant;
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
