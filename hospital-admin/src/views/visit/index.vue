<template>
  <div class="visit-page">
    <el-card class="mb-15">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="患者姓名">
          <el-input v-model="searchForm.patientName" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <el-form-item label="医生">
          <el-select v-model="searchForm.doctorId" placeholder="请选择医生" clearable filterable>
            <el-option v-for="doc in doctorList" :key="doc.id" :label="doc.name" :value="doc.id" />
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
        <el-button type="primary" @click="handleAdd" v-permission="'visit:create'">
          <el-icon><Plus /></el-icon>新增就诊
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column type="index" width="60" label="序号" />
        <el-table-column prop="patientName" label="患者姓名" min-width="100" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="deptName" label="科室" width="120" />
        <el-table-column prop="diagnosis" label="诊断结果" min-width="200" show-overflow-tooltip />
        <el-table-column prop="visitTime" label="就诊时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.visitTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'primary'">
              {{ row.status === 1 ? '已完成' : '进行中' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleViewAdvice(row)" v-permission="'visit:view'">查看医嘱</el-button>
            <el-button link type="success" @click="handleAddAdvice(row)" v-permission="'visit:advice'">录入医嘱</el-button>
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

    <el-dialog v-model="dialogVisible" title="新增就诊" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="患者" prop="patientId">
          <el-select v-model="form.patientId" placeholder="请选择患者" style="width: 100%;" filterable>
            <el-option v-for="p in patientList" :key="p.id" :label="p.name" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="医生" prop="doctorId">
          <el-select v-model="form.doctorId" placeholder="请选择医生" style="width: 100%;" filterable>
            <el-option v-for="doc in doctorList" :key="doc.id" :label="doc.name" :value="doc.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="诊断结果" prop="diagnosis">
          <el-input v-model="form.diagnosis" type="textarea":rows="3" placeholder="请输入诊断结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="adviceDialogVisible" title="医嘱录入" width="700px">
      <el-form :model="adviceForm" :rules="adviceRules" ref="adviceRef" label-width="100px">
        <el-form-item label="诊断结果" prop="diagnosis">
          <el-input v-model="adviceForm.diagnosis" type="textarea":rows="3" placeholder="请输入诊断结果" />
        </el-form-item>
        <el-form-item label="医嘱内容" prop="advice">
          <el-input v-model="adviceForm.advice" type="textarea":rows="5" placeholder="请输入医嘱内容" />
        </el-form-item>
        <el-form-item label="用药建议">
          <el-input v-model="adviceForm.medication" type="textarea":rows="3" placeholder="请输入用药建议" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="adviceDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAdviceSubmit" :loading="adviceLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="医嘱详情" width="700px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="患者">{{ currentVisit?.patientName }}</el-descriptions-item>
        <el-descriptions-item label="医生">{{ currentVisit?.doctorName }}</el-descriptions-item>
        <el-descriptions-item label="科室">{{ currentVisit?.deptName }}</el-descriptions-item>
        <el-descriptions-item label="诊断结果">{{ currentVisit?.diagnosis || '-' }}</el-descriptions-item>
        <el-descriptions-item label="医嘱内容">{{ currentVisit?.advice || '-' }}</el-descriptions-item>
        <el-descriptions-item label="用药建议">{{ currentVisit?.medication || '-' }}</el-descriptions-item>
        <el-descriptions-item label="就诊时间">{{ currentVisit?.visitTime ? formatDate(currentVisit.visitTime) : '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getVisitList, createVisit, updateVisit, getVisitDetail } from '@/api/visit'
import { getDoctorList } from '@/api/doctor'
import { getUserList } from '@/api/user'
import { formatDate } from '@/utils/format'
import type { Visit } from '@/types'

const loading = ref(false)
const tableData = ref<Visit[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const doctorList = ref<{ id: number; name: string }[]>([])
const patientList = ref<{ id: number; name: string }[]>([])

const searchForm = reactive({
  patientName: '',
  doctorId: undefined as number | undefined
})

const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref()

const form = reactive({
  patientId: undefined as number | undefined,
  doctorId: undefined as number | undefined,
  diagnosis: '',
  status: 0
})

const rules = {
  patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
  diagnosis: [{ required: true, message: '请输入诊断结果', trigger: 'blur' }]
}

const adviceDialogVisible = ref(false)
const adviceLoading = ref(false)
const adviceRef = ref()
const currentVisitId = ref(0)

const adviceForm = reactive({
  diagnosis: '',
  advice: '',
  medication: '',
  status: 1
})

const adviceRules = {
  diagnosis: [{ required: true, message: '请输入诊断结果', trigger: 'blur' }],
  advice: [{ required: true, message: '请输入医嘱内容', trigger: 'blur' }]
}

const viewDialogVisible = ref(false)
const currentVisit = ref<Visit | null>(null)

const loadDoctors = async () => {
  try {
    const res = await getDoctorList({ page: 1, pageSize: 100 }) as any
    doctorList.value = res.list.map((d: any) => ({ id: d.id, name: d.name }))
  } catch {
    doctorList.value = [{ id: 1, name: '张医生' }, { id: 2, name: '李医生' }]
  }
}

const loadPatients = async () => {
  try {
    const res = await getUserList({ page: 1, pageSize: 100 }) as any
    patientList.value = res.list.map((p: any) => ({ id: p.id, name: p.name }))
  } catch {
    patientList.value = [{ id: 1, name: '张三' }, { id: 2, name: '李四' }]
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getVisitList({
      page: page.value,
      pageSize: pageSize.value,
      ...searchForm
    }) as any
    tableData.value = res.list
    total.value = res.total
  } catch {
    tableData.value = [
      { id: 1, patientName: '张三', doctorName: '张医生', deptName: '内科', diagnosis: '上呼吸道感染', advice: '多休息，多喝水', medication: '感冒灵颗粒', visitTime: '2024-05-01 09:00:00', status: 1 },
      { id: 2, patientName: '李四', doctorName: '李医生', deptName: '外科', diagnosis: '骨折', advice: '石膏固定，定期复查', medication: '', visitTime: '2024-05-01 10:00:00', status: 1 }
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
  searchForm.patientName = ''
  searchForm.doctorId = undefined
  handleSearch()
}

const handleAdd = () => {
  Object.assign(form, {
    patientId: undefined,
    doctorId: undefined,
    diagnosis: '',
    status: 0
  })
  dialogVisible.value = true
}

const handleAddAdvice = (row: Visit) => {
  currentVisitId.value = row.id
  Object.assign(adviceForm, {
    diagnosis: row.diagnosis || '',
    advice: row.advice || '',
    medication: '',
    status: 1
  })
  adviceDialogVisible.value = true
}

const handleViewAdvice = async (row: Visit) => {
  try {
    const res = await getVisitDetail(row.id) as any
    currentVisit.value = res
  } catch {
    currentVisit.value = row
  }
  viewDialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    await createVisit(form)
    ElMessage.success('创建成功')
    dialogVisible.value = false
    loadData()
  } finally {
    submitLoading.value = false
  }
}

const handleAdviceSubmit = async () => {
  await adviceRef.value.validate()
  adviceLoading.value = true
  try {
    await updateVisit(currentVisitId.value, adviceForm)
    ElMessage.success('医嘱录入成功')
    adviceDialogVisible.value = false
    loadData()
  } finally {
    adviceLoading.value = false
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
  loadDoctors()
  loadPatients()
  loadData()
})
</script>
