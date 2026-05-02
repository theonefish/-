<template>
  <view class="edit-page">
    <view class="form-card">
      <view class="input-group">
        <text class="label">姓名</text>
        <input v-model="form.name" placeholder="请输入姓名" class="input-field" />
      </view>
      <view class="input-group">
        <text class="label">身份证号</text>
        <input v-model="form.cardNo" placeholder="请输入身份证号" class="input-field" />
      </view>
      <view class="input-group">
        <text class="label">手机号</text>
        <input v-model="form.phone" placeholder="请输入手机号" type="number" class="input-field" />
      </view>
      <view class="input-group">
        <text class="label">年龄</text>
        <input v-model.number="form.age" type="number" placeholder="请输入年龄" class="input-field" />
      </view>
      <view class="input-group">
        <text class="label">性别</text>
        <picker mode="selector" :range="['女', '男', '未知']" :value="form.sex || 2" @change="onSexChange">
          <view class="picker-value">{{ ['女', '男', '未知'][form.sex || 2] }}</view>
        </picker>
      </view>
      <view class="input-group">
        <text class="label">关系</text>
        <input v-model="form.relation" placeholder="如：本人、父母、子女" class="input-field" />
      </view>
      <view class="input-group row">
        <text class="label">设为默认就诊人</text>
        <switch :checked="form.isDefault === '1'" color="#0052d9"
          @change="form.isDefault = ($event.detail.value ? '1' : '0')" />
      </view>
      <button class="btn-primary" @click="submit">保存</button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores/user'
import { addVisitUser, updateVisitUser } from '@/api/visit-user'

const userStore = useUserStore()
const form = reactive<any>({ name: '', cardNo: '', phone: '', age: null, sex: 2, relation: '', isDefault: '0' })
let isEdit = false
let editId = 0

function onSexChange(e: any) {
  form.sex = e.detail.value
}

async function submit() {
  if (!form.name || !form.cardNo || !form.phone) {
    uni.showToast({ title: '请填写完整', icon: 'none' })
    return
  }
  const data = {
    visitname: form.name,
    idCard: form.cardNo,
    phone: form.phone,
    sex: String(form.sex || 2),
    birthday: '',
    userId: userStore.userId
  }
  try {
    if (isEdit) {
      await updateVisitUser(editId, data)
    } else {
      await addVisitUser(data)
    }
    uni.showToast({ title: '保存成功' })
    setTimeout(() => uni.navigateBack(), 800)
  } catch (e) {
    console.error(e)
  }
}

onLoad((opt: any) => {
  if (opt.data) {
    const parsed = JSON.parse(decodeURIComponent(opt.data))
    Object.assign(form, parsed)
    isEdit = true
    editId = parsed.visitId
  }
})
</script>

<style lang="scss" scoped>
@import '../../uni.scss';

.edit-page {
  padding: 24rpx;
  background: $surface;
  min-height: 100vh;
}

.form-card {
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 32rpx;
  box-shadow: $shadow-card;
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

  .picker-value {
    height: 96rpx;
    line-height: 96rpx;
    padding: 0 24rpx;
    background: $surface-container-low;
    border: 2rpx solid $outline-variant;
    border-radius: $radius-md;
    font-size: 28rpx;
    color: $on-surface;
    box-sizing: border-box;
  }

  &.row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 0;

    .label {
      margin: 0;
    }
  }
}

.btn-primary {
  margin-top: 20rpx;
  width: 100%;
}
</style>
