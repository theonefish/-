<template>
  <div class="user-page">
    <el-card class="mb-15">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="请输入姓名/手机号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
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
        <el-button type="primary" @click="handleAdd" v-permission="'user:create'">
          <el-icon><Plus /></el-icon>新增患者
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column type="index" width="60" label="序号" />
        <el-table-column prop="name" label="姓名" min-width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="idCard" label="身份证号" width="180" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            {{ formatGender(row.gender) }}
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="(val: string | number | boolean) => handleStatusChange(row, val as number)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)" v-permission="'user:update'">编辑</el-button>
            <el-button link type="warning" @click="handleResetPassword(row)" v-permission="'user:reset'">重置密码</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-permission="'user:delete'">删除</el-button>
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
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入患者姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="form.age" :min="0" :max="150" />
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
import { getUserList, createUser, updateUser, deleteUser, resetUserPassword } from '@/api/user'
import { formatDate, formatGender } from '@/utils/format'
import type { Patient } from '@/types'

const loading = ref(false)
const tableData = ref<Patient[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  keyword: '',
  status: undefined as number | undefined
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()
const isEdit = ref(false)
const currentId = ref(0)

const form = reactive({
  name: '',
  phone: '',
  idCard: '',
  gender: 1,
  age: 30,
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入患者姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  idCard: [
    { pattern: /^\d{17}[\dXx]$/, message: '身份证号格式不正确', trigger: 'blur' }
  ]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getUserList({
      page: page.value,
      pageSize: pageSize.value,
      ...searchForm
    }) as any
    tableData.value = res.list
    total.value = res.total
  } catch {
    tableData.value = [
      { id: 1, name: '张三', phone: '13800138001', idCard: '110101199001011234', gender: 1, age: 34, status: 1, createTime: '2024-01-15 10:00:00' },
      { id: 2, name: '李四', phone: '13800138002', idCard: '110101199501015678', gender: 0, age: 29, status: 1, createTime: '2024-02-20 14:00:00' }
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
  searchForm.keyword = ''
  searchForm.status = undefined
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增患者'
  Object.assign(form, {
    name: '',
    phone: '',
    idCard: '',
    gender: 1,
    age: 30,
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row: Patient) => {
  isEdit.value = true
  currentId.value = row.id
  dialogTitle.value = '编辑患者'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row: Patient) => {
  try {
    await ElMessageBox.confirm(`确定删除患者"${row.name}"吗？`, '提示', { type: 'warning' })
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // cancel
  }
}

const handleResetPassword = async (row: Patient) => {
  try {
    await ElMessageBox.confirm(`确定重置患者"${row.name}"的密码吗？`, '提示', { type: 'warning' })
    await resetUserPassword(row.id)
    ElMessage.success('密码重置成功')
  } catch {
    // cancel
  }
}

const handleStatusChange = async (row: Patient, val: number) => {
  try {
    await updateUser(row.id, { status: val })
    ElMessage.success('状态更新成功')
  } catch {
    row.status = val === 1 ? 0 : 1
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateUser(currentId.value, form)
    } else {
      await createUser(form)
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
