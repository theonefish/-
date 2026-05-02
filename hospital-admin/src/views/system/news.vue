<template>
  <div class="news-page">
    <el-card class="mb-15">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="已发布" :value="1" />
            <el-option label="草稿" :value="0" />
          </el-select>
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
      <div class="table-toolbar">
        <el-button type="primary" @click="handleAdd" v-permission="'system:news:create'">
          <el-icon><Plus /></el-icon>新增资讯
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column type="index" width="60" label="序号" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="cover" label="封面" width="120">
          <template #default="{ row }">
            <el-image v-if="row.cover" :src="row.cover" style="width: 80px; height: 50px;" fit="cover" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="recommended" label="首页推荐" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.recommended"
              :active-value="true"
              :inactive-value="false"
              @change="(val: string | number | boolean) => handleRecommendChange(row, val as boolean)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)" v-permission="'system:news:update'">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-permission="'system:news:delete'">删除</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="封面">
          <el-upload
            class="avatar-uploader"
            action="#"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleCoverChange"
          >
            <img v-if="form.cover" :src="form.cover" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="首页推荐">
          <el-switch v-model="form.recommended" :active-value="true" :inactive-value="false" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea":rows="10" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNewsList, createNews, updateNews, deleteNews } from '@/api/system'
import { formatDate } from '@/utils/format'
import type { News } from '@/types'

const loading = ref(false)
const tableData = ref<News[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  title: '',
  status: undefined as number | undefined
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()
const isEdit = ref(false)
const currentId = ref(0)

const form = reactive({
  title: '',
  content: '',
  cover: '',
  recommended: false,
  status: 1
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getNewsList({
      page: page.value,
      pageSize: pageSize.value,
      ...searchForm
    }) as any
    tableData.value = res.list
    total.value = res.total
  } catch {
    tableData.value = [
      { id: 1, title: '医院开展新技术培训', content: '...', cover: '', recommended: true, status: 1, createTime: '2024-04-20 10:00:00' },
      { id: 2, title: '春季健康讲座通知', content: '...', cover: '', recommended: false, status: 1, createTime: '2024-04-18 14:00:00' }
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
  searchForm.title = ''
  searchForm.status = undefined
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增资讯'
  Object.assign(form, {
    title: '',
    content: '',
    cover: '',
    recommended: false,
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row: News) => {
  isEdit.value = true
  currentId.value = row.id
  dialogTitle.value = '编辑资讯'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row: News) => {
  try {
    await ElMessageBox.confirm(`确定删除资讯"${row.title}"吗？`, '提示', { type: 'warning' })
    await deleteNews(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // cancel
  }
}

const handleRecommendChange = async (row: News, val: boolean) => {
  try {
    await updateNews(row.id, { recommended: val })
    ElMessage.success('更新成功')
  } catch {
    row.recommended = !val
  }
}

const handleCoverChange = (file: any) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    form.cover = e.target?.result as string
  }
  reader.readAsDataURL(file.raw)
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateNews(currentId.value, form)
    } else {
      await createNews(form)
    }
    ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
    dialogVisible.value = false
    loadData()
  } finally {
    submitLoading.value = false
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

<style scoped lang="scss">
.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);

    &:hover {
      border-color: var(--el-color-primary);
    }
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 100px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 178px;
  height: 100px;
  display: block;
  object-fit: cover;
}
</style>
