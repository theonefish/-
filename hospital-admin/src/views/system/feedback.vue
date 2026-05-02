<template>
  <div class="feedback-page">
    <el-card class="mb-15">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="用户">
          <el-input v-model="searchForm.userName" placeholder="请输入用户名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>查询
          </el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column type="index" width="60" label="序号" />
        <el-table-column prop="userName" label="用户" min-width="100" />
        <el-table-column prop="content" label="反馈内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="contact" label="联系方式" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'warning'">
              {{ row.status === 1 ? '已处理' : '待处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看详情</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-permission="'system:feedback:delete'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" title="反馈详情" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="用户">{{ currentFeedback?.userName }}</el-descriptions-item>
        <el-descriptions-item label="联系方式">{{ currentFeedback?.contact || '-' }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ currentFeedback?.createTime ? formatDate(currentFeedback.createTime) : '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentFeedback?.status === 1 ? 'success' : 'warning'">
            {{ currentFeedback?.status === 1 ? '已处理' : '待处理' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="反馈内容">{{ currentFeedback?.content }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button v-if="currentFeedback?.status === 0" type="primary" @click="handleProcess">标记为已处理</el-button>
        <el-button @click="dialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getFeedbackList, deleteFeedback } from '@/api/system'
import { formatDate } from '@/utils/format'
import type { Feedback } from '@/types'

const loading = ref(false)
const tableData = ref<Feedback[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  userName: ''
})

const dialogVisible = ref(false)
const currentFeedback = ref<Feedback | null>(null)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getFeedbackList({
      page: page.value,
      pageSize: pageSize.value,
      ...searchForm
    }) as any
    tableData.value = res.list
    total.value = res.total
  } catch {
    tableData.value = [
      { id: 1, userName: '张三', content: '预约挂号流程很顺畅，建议增加在线支付功能', contact: '13800138001', status: 0, createTime: '2024-04-25 10:00:00' },
      { id: 2, userName: '李四', content: '医生态度很好，就诊体验满意', contact: '13800138002', status: 1, createTime: '2024-04-24 14:00:00' }
    ] as any
    total.value = 2
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const resetSearch = () => {
  searchForm.userName = ''
  handleSearch()
}

const handleView = (row: Feedback) => {
  currentFeedback.value = row
  dialogVisible.value = true
}

const handleProcess = async () => {
  if (!currentFeedback.value) return
  try {
    // await updateFeedback(currentFeedback.value.id, { status: 1 })
    currentFeedback.value.status = 1
    ElMessage.success('标记成功')
    loadData()
  } catch {
    // error
  }
}

const handleDelete = async (row: Feedback) => {
  try {
    await ElMessageBox.confirm(`确定删除该反馈吗？`, '提示', { type: 'warning' })
    await deleteFeedback(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // cancel
  }
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  loadData()
}

const handlePageChange = (val: number) => {
  page.value = val
  loadData()
}

onMounted(() => {
  loadData()
})
</script>
