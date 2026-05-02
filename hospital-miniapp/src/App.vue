<script setup lang="ts">
import { onLaunch } from '@dcloudio/uni-app'

onLaunch(() => {
  console.log('App Launch')
})
</script>

<style lang="scss">
@import './uni.scss';

/* 全局页面基础样式 */
page {
  background-color: $surface;
  font-family: 'Public Sans', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  color: $on-surface;
  -webkit-font-smoothing: antialiased;
}

/* 通用卡片样式 */
.card {
  background: $surface-container-lowest;
  border-radius: $radius-lg;
  padding: $container-padding;
  box-shadow: $shadow-card;
}

/* 通用区块标题 */
.section-title {
  font-size: 32rpx;
  font-weight: 700;
  color: $on-surface;
  margin-bottom: $stack-gap;
}

/* 主按钮样式 */
.btn-primary {
  height: 96rpx;
  line-height: 96rpx;
  background: $primary;
  color: $on-primary;
  border-radius: $radius-md;
  font-size: 32rpx;
  font-weight: 600;
  border: none;
  box-shadow: $shadow-float;
  transition: all 0.2s ease;

  &:active {
    transform: translateY(2rpx);
    box-shadow: $shadow-card;
  }

  &:disabled {
    background: $surface-dim;
    color: $on-surface-variant;
    box-shadow: none;
  }
}

/* 次要按钮 */
.btn-secondary {
  height: 96rpx;
  line-height: 96rpx;
  background: $primary-fixed;
  color: $on-primary-fixed-variant;
  border-radius: $radius-md;
  font-size: 32rpx;
  font-weight: 600;
  border: none;

  &:active {
    background: $primary-fixed-dim;
  }
}

/* 幽灵按钮 */
.btn-ghost {
  height: 96rpx;
  line-height: 96rpx;
  background: transparent;
  color: $primary;
  border-radius: $radius-md;
  font-size: 32rpx;
  font-weight: 600;
  border: 2rpx solid $outline-variant;

  &:active {
    background: $surface-container-low;
  }
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 40rpx;

  .empty-icon {
    font-size: 120rpx;
    margin-bottom: 24rpx;
    opacity: 0.3;
  }

  .empty-text {
    font-size: 28rpx;
    color: $on-surface-variant;
    text-align: center;
  }
}

/* 通用输入框 */
.input-field {
  height: 96rpx;
  padding: 0 24rpx;
  background: $surface-container-low;
  border: 2rpx solid $outline-variant;
  border-radius: $radius-md;
  font-size: 28rpx;
  color: $on-surface;
  transition: border-color 0.2s;

  &:focus {
    border-color: $primary;
    background: $surface-container-lowest;
  }

  &::placeholder {
    color: $on-surface-variant;
  }
}

/* 标签样式 */
.tag {
  display: inline-flex;
  align-items: center;
  padding: 6rpx 16rpx;
  border-radius: $radius-sm;
  font-size: 22rpx;
  font-weight: 500;

  &.tag-primary {
    background: $primary-fixed;
    color: $on-primary-fixed-variant;
  }

  &.tag-success {
    background: $secondary-fixed;
    color: $on-secondary-fixed-variant;
  }

  &.tag-error {
    background: $error-container;
    color: $on-error-container;
  }

  &.tag-warning {
    background: $tertiary-fixed;
    color: $on-tertiary-fixed-variant;
  }
}

/* 列表项通用样式 */
.list-item {
  display: flex;
  align-items: center;
  padding: 28rpx 32rpx;
  background: $surface-container-lowest;
  border-bottom: 1rpx solid $surface-container;

  &:last-child {
    border-bottom: none;
  }

  .list-item-content {
    flex: 1;
  }

  .list-item-label {
    font-size: 30rpx;
    color: $on-surface;
  }

  .list-item-value {
    font-size: 28rpx;
    color: $on-surface-variant;
    margin-top: 4rpx;
  }

  .list-item-arrow {
    font-size: 28rpx;
    color: $outline;
    margin-left: 16rpx;
  }
}
</style>
