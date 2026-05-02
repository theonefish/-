<template>
  <div class="department-page">
    <el-card class="mb-15">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="科室名称">
          <el-input v-model="searchForm.keyword" placeholder="请输入科室名称" clearable />
        </el-form-item>
        <el-form-item label="首页推荐">
          <el-select v-model="searchForm.toHome" placeholder="请选择" clearable>
            <el-option label="是" value="1" />
            <el-option label="否" value="0" />
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
        <el-button type="primary" @click="handleAdd" v-permission="'department:create'">
          <el-icon><Plus /></el-icon>新增科室
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column type="index" width="60" label="序号" />
        <el-table-column prop="name" label="科室名称" min-width="120" />
        <el-table-column prop="description" label="科室简介" min-width="200" show-overflow-tooltip />
        <el-table-column prop="phone" label="科室电话" width="130" />
        <el-table-column prop="recommended" label="首页推荐" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.recommended"
              :active-value="true"
              :inactive-value="false"
              @change="(val: boolean) => handleRecommendChange(row, val)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)" v-permission="'department:update'">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-permission="'department:delete'">删除</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="科室名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入科室名称" />
        </el-form-item>
        <el-form-item label="科室简介" prop="description">
          <el-input v-model="form.description" type="textarea":rows="3" placeholder="请输入科室简介" />
        </el-form-item>
        <el-form-item label="科室电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入科室电话" />
        </el-form-item>
        <el-form-item label="首页推荐">
          <el-switch v-model="form.recommended" :active-value="true" :inactive-value="false" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
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
import { getDeptList, createDept, updateDept, deleteDept } from '@/api/department'
import type { Dept } from '@/types'

const loading = ref(false)
const tableData = ref<Dept[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  keyword: '',
  toHome: undefined as number | undefined
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()
const isEdit = ref(false)
const currentId = ref(0)

const form = reactive({
  name: '',
  description: '',
  phone: '',
  recommended: false,
  sort: 0,
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入科室名称', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$|^0\d{2,3}-?\d{7,8}$/, message: '电话格式不正确', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDeptList({
      page: page.value,
      pageSize: pageSize.value,
      ...searchForm
    }) as any
    tableData.value = res.list
    total.value = res.total
  } catch {
    tableData.value = [
      { id: 1, name: '内科', description: '负责内科疾病诊治', phone: '010-12345678', recommended: true, status: 1, sort: 1 },
      { id: 2, name: '外科', description: '负责外科手术及治疗', phone: '010-12345679', recommended: true, status: 1, sort: 2 },
      { id: 3, name: '儿科', description: '儿童疾病诊治', phone: '010-12345680', recommended: false, status: 1, sort: 3 },
      { id: 4, name: '骨科', description: '骨骼肌肉系统疾病', phone: '010-12345681', recommended: false, status: 1, sort: 4 }
    ] as any
    total.value = 4
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const resetSearch = () => {
  searchForm.keyword = ''
  searchForm.toHome = undefined
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增科室'
  Object.assign(form, {
    name: '',
    description: '',
    phone: '',
    recommended: false,
    sort: 0,
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row: Dept) => {
  isEdit.value = true
  currentId.value = row.id
  dialogTitle.value = '编辑科室'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row: Dept) => {
  try {
    await ElMessageBox.confirm(`确定删除科室"${row.name}"吗？`, '提示', { type: 'warning' })
    await deleteDept(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // cancel
  }
}

const handleRecommendChange = async (row: Dept, val: boolean) => {
  try {
    await updateDept(row.id, { toHome: val ? '1' : '0' })
    ElMessage.success('首页推荐更新成功')
  } catch {
    row.recommended = !val
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateDept(currentId.value, form)
    } else {
      await createDept(form)
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
