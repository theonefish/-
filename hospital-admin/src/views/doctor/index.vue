<template>
  <div class="doctor-page">
    <el-card class="mb-15">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="请输入医生姓名/职称" clearable />
        </el-form-item>
        <el-form-item label="科室">
          <el-select v-model="searchForm.deptId" placeholder="请选择科室" clearable>
            <el-option v-for="dept in deptList" :key="dept.id" :label="dept.name" :value="dept.id" />
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
        <el-button type="primary" @click="handleAdd" v-permission="'doctor:create'">
          <el-icon><Plus /></el-icon>新增医生
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column type="index" width="60" label="序号" />
        <el-table-column prop="name" label="姓名" min-width="100" />
        <el-table-column prop="title" label="职称" width="120" />
        <el-table-column prop="deptName" label="所属科室" width="120" />
        <el-table-column prop="specialty" label="擅长领域" min-width="200" show-overflow-tooltip />
        <el-table-column prop="introduction" label="简介" min-width="200" show-overflow-tooltip />
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
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)" v-permission="'doctor:update'">编辑</el-button>
            <el-button link type="warning" @click="handleResetPassword(row)" v-permission="'doctor:reset'">重置密码</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-permission="'doctor:delete'">删除</el-button>
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
          <el-input v-model="form.name" placeholder="请输入医生姓名" />
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-select v-model="form.title" placeholder="请选择职称" style="width: 100%;">
            <el-option label="主任医师" value="主任医师" />
            <el-option label="副主任医师" value="副主任医师" />
            <el-option label="主治医师" value="主治医师" />
            <el-option label="医师" value="医师" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属科室" prop="deptId">
          <el-select v-model="form.deptId" placeholder="请选择科室" style="width: 100%;">
            <el-option v-for="dept in deptList" :key="dept.id" :label="dept.name" :value="dept.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="擅长领域" prop="specialty">
          <el-input v-model="form.specialty" type="textarea":rows="2" placeholder="请输入擅长领域" />
        </el-form-item>
        <el-form-item label="医生简介" prop="introduction">
          <el-input v-model="form.introduction" type="textarea":rows="3" placeholder="请输入医生简介" />
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
import { getDoctorList, createDoctor, updateDoctor, deleteDoctor, resetDoctorPassword } from '@/api/doctor'
import { getDeptList } from '@/api/department'
import type { Doctor } from '@/types'

const loading = ref(false)
const tableData = ref<Doctor[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const deptList = ref<{ id: number; name: string }[]>([])

const searchForm = reactive({
  keyword: '',
  deptId: undefined as number | undefined
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()
const isEdit = ref(false)
const currentId = ref(0)

const form = reactive({
  name: '',
  title: '',
  deptId: undefined as number | undefined,
  specialty: '',
  introduction: '',
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入医生姓名', trigger: 'blur' }],
  title: [{ required: true, message: '请选择职称', trigger: 'change' }],
  deptId: [{ required: true, message: '请选择所属科室', trigger: 'change' }]
}

const loadDepts = async () => {
  try {
    const res = await getDeptList({ page: 1, pageSize: 100 }) as any
    deptList.value = res.list
  } catch {
    deptList.value = [
      { id: 1, name: '内科' },
      { id: 2, name: '外科' },
      { id: 3, name: '儿科' },
      { id: 4, name: '骨科' }
    ]
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDoctorList({
      page: page.value,
      pageSize: pageSize.value,
      ...searchForm
    }) as any
    tableData.value = res.list
    total.value = res.total
  } catch {
    tableData.value = [
      { id: 1, name: '张医生', title: '主任医师', deptId: 1, deptName: '内科', avatar: '', specialty: '心血管疾病', introduction: '从事心血管临床工作20年', status: 1 },
      { id: 2, name: '李医生', title: '副主任医师', deptId: 2, deptName: '外科', avatar: '', specialty: '普外科手术', introduction: '擅长微创手术', status: 1 }
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
  searchForm.deptId = undefined
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增医生'
  Object.assign(form, {
    name: '',
    title: '',
    deptId: undefined,
    specialty: '',
    introduction: '',
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row: Doctor) => {
  isEdit.value = true
  currentId.value = row.id
  dialogTitle.value = '编辑医生'
  Object.assign(form, {
    name: row.name,
    title: row.title,
    deptId: row.deptId,
    specialty: row.specialty,
    introduction: row.introduction,
    status: row.status
  })
  dialogVisible.value = true
}

const handleDelete = async (row: Doctor) => {
  try {
    await ElMessageBox.confirm(`确定删除医生"${row.name}"吗？`, '提示', { type: 'warning' })
    await deleteDoctor(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // cancel
  }
}

const handleResetPassword = async (row: Doctor) => {
  try {
    await ElMessageBox.confirm(`确定重置医生"${row.name}"的密码吗？`, '提示', { type: 'warning' })
    await resetDoctorPassword(row.id)
    ElMessage.success('密码重置成功')
  } catch {
    // cancel
  }
}

const handleStatusChange = async (row: Doctor, val: number) => {
  try {
    await updateDoctor(row.id, { status: val })
    ElMessage.success('状态更新成功')
  } catch {
    row.status = val === 1 ? 0 : 1
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    const dept = deptList.value.find(d => d.id === form.deptId)
    const data = { ...form, deptName: dept?.name }
    if (isEdit.value) {
      await updateDoctor(currentId.value, data)
    } else {
      await createDoctor(data)
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
  loadDepts()
  loadData()
})
</script>
