<template>
  <div class="appointment-page">
    <el-card class="mb-15">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <el-form-item label="医生">
          <el-select v-model="searchForm.doctorId" placeholder="请选择医生" clearable filterable>
            <el-option v-for="doc in doctorList" :key="doc.id" :label="doc.name" :value="doc.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="科室">
          <el-select v-model="searchForm.deptId" placeholder="请选择科室" clearable>
            <el-option v-for="dept in deptList" :key="dept.id" :label="dept.name" :value="dept.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="handleDateRangeChange"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="待就诊" :value="0" />
            <el-option label="已就诊" :value="1" />
            <el-option label="已取消" :value="2" />
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
        <el-button type="primary" @click="handleAdd" v-permission="'appointment:create'">
          <el-icon><Plus /></el-icon>新增预约
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column type="index" width="60" label="序号" />
        <el-table-column prop="patientName" label="患者姓名" min-width="100" />
        <el-table-column prop="patientPhone" label="患者电话" width="130" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="deptName" label="科室" width="120" />
        <el-table-column prop="date" label="预约日期" width="120" />
        <el-table-column prop="period" label="时段" width="100">
          <template #default="{ row }">
            <el-tag :type="row.period === '上午' ? 'success' : 'warning'">{{ row.period }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status) as any">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="预约时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleCall(row)" v-permission="'appointment:call'" :disabled="row.status !== 0">
              叫号
            </el-button>
            <el-button link type="success" @click="handleExam(row)" v-permission="'appointment:exam'">检查</el-button>
            <el-button link type="warning" @click="handleLab(row)" v-permission="'appointment:lab'">检验</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-permission="'appointment:delete'">删除</el-button>
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

    <el-dialog v-model="dialogVisible" title="新增预约" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="患者" prop="patientId">
          <el-select v-model="form.patientId" placeholder="请选择患者" style="width: 100%;" filterable>
            <el-option v-for="p in patientList" :key="p.id" :label="p.name" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="医生" prop="doctorId">
          <el-select v-model="form.doctorId" placeholder="请选择医生" style="width: 100%;" filterable @change="handleDoctorChange">
            <el-option v-for="doc in doctorList" :key="doc.id" :label="doc.name" :value="doc.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期" prop="date">
          <el-date-picker
            v-model="form.date"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 100%;"
            @change="handleDateChange"
          />
        </el-form-item>
        <el-form-item label="时段" prop="period">
          <el-radio-group v-model="form.period" @change="handlePeriodChange">
            <el-radio-button label="上午" />
            <el-radio-button label="下午" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="剩余号源">
          <el-tag :type="remainingQuota > 0 ? 'success' : 'danger'">{{ remainingQuota }} 个</el-tag>
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
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAppointmentList, createAppointment, deleteAppointment, callNumber } from '@/api/appointment'
import { getDoctorList } from '@/api/doctor'
import { getDeptList } from '@/api/department'
import { getUserList } from '@/api/user'
import { getScheduleList } from '@/api/schedule'
import { formatDate } from '@/utils/format'
import type { Appointment } from '@/types'

const router = useRouter()
const loading = ref(false)
const tableData = ref<Appointment[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const doctorList = ref<{ id: number; name: string }[]>([])
const deptList = ref<{ id: number; name: string }[]>([])
const patientList = ref<{ id: number; name: string }[]>([])
const scheduleList = ref<any[]>([])
const remainingQuota = ref(0)

const searchForm = reactive({
  keyword: '',
  doctorId: undefined as number | undefined,
  deptId: undefined as number | undefined,
  startDate: '',
  endDate: '',
  status: undefined as number | undefined
})

const dateRange = ref<string[]>([])

const handleDateRangeChange = (val: string[]) => {
  if (val && val.length === 2) {
    searchForm.startDate = val[0]
    searchForm.endDate = val[1]
  } else {
    searchForm.startDate = ''
    searchForm.endDate = ''
  }
}

const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref()

const form = reactive({
  patientId: undefined as number | undefined,
  doctorId: undefined as number | undefined,
  date: '',
  period: '上午'
})

const rules = {
  patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
  date: [{ required: true, message: '请选择日期', trigger: 'change' }],
  period: [{ required: true, message: '请选择时段', trigger: 'change' }]
}

const getStatusType = (status: number) => {
  const map: Record<number, string> = { 0: 'primary', 1: 'success', 2: 'info' }
  return map[status] || 'info'
}

const getStatusText = (status: number) => {
  const map: Record<number, string> = { 0: '待就诊', 1: '已就诊', 2: '已取消' }
  return map[status] || '未知'
}

const loadDoctors = async () => {
  try {
    const res = await getDoctorList({ page: 1, pageSize: 100 }) as any
    doctorList.value = res.list.map((d: any) => ({ id: d.id, name: d.name }))
  } catch {
    doctorList.value = [{ id: 1, name: '张医生' }, { id: 2, name: '李医生' }]
  }
}

const loadDepts = async () => {
  try {
    const res = await getDeptList({ page: 1, pageSize: 100 }) as any
    deptList.value = res.list.map((d: any) => ({ id: d.id, name: d.name }))
  } catch {
    deptList.value = [{ id: 1, name: '内科' }, { id: 2, name: '外科' }]
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

const loadSchedules = async () => {
  try {
    const res = await getScheduleList({ page: 1, pageSize: 1000 }) as any
    scheduleList.value = res.list
  } catch {
    scheduleList.value = []
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAppointmentList({
      page: page.value,
      pageSize: pageSize.value,
      ...searchForm
    }) as any
    tableData.value = res.list
    total.value = res.total
  } catch {
    tableData.value = [
      { id: 1, patientName: '张三', patientPhone: '13800138001', doctorName: '张医生', deptName: '内科', date: '2024-05-01', period: '上午', status: 0, createTime: '2024-04-25 10:00:00' },
      { id: 2, patientName: '李四', patientPhone: '13800138002', doctorName: '李医生', deptName: '外科', date: '2024-05-01', period: '下午', status: 1, createTime: '2024-04-25 11:00:00' }
    ] as any
    total.value = 2
  } finally {
    loading.value = false
  }
}

const updateRemainingQuota = () => {
  if (!form.doctorId || !form.date || !form.period) {
    remainingQuota.value = 0
    return
  }
  const schedule = scheduleList.value.find(
    s => s.doctorId === form.doctorId && s.date === form.date && s.period === form.period
  )
  remainingQuota.value = schedule ? schedule.quota - schedule.booked : 0
}

const handleDoctorChange = () => {
  updateRemainingQuota()
}

const handleDateChange = () => {
  updateRemainingQuota()
}

const handlePeriodChange = () => {
  updateRemainingQuota()
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const resetSearch = () => {
  searchForm.keyword = ''
  searchForm.doctorId = undefined
  searchForm.deptId = undefined
  searchForm.startDate = ''
  searchForm.endDate = ''
  searchForm.status = undefined
  dateRange.value = []
  handleSearch()
}

const handleAdd = () => {
  Object.assign(form, {
    patientId: undefined,
    doctorId: undefined,
    date: '',
    period: '上午'
  })
  remainingQuota.value = 0
  dialogVisible.value = true
}

const handleCall = async (row: Appointment) => {
  try {
    await callNumber(row.id)
    ElMessage.success('叫号成功')
    loadData()
  } catch {
    ElMessage.error('叫号失败')
  }
}

const handleExam = (row: Appointment) => {
  router.push(`/exam?patientId=${row.id}`)
}

const handleLab = (row: Appointment) => {
  router.push(`/lab?patientId=${row.id}`)
}

const handleDelete = async (row: Appointment) => {
  try {
    await ElMessageBox.confirm(`确定删除该预约吗？`, '提示', { type: 'warning' })
    await deleteAppointment(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // cancel
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  if (remainingQuota.value <= 0) {
    ElMessage.warning('该时段号源已满')
    return
  }
  submitLoading.value = true
  try {
    await createAppointment(form)
    ElMessage.success('预约成功')
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
  loadDoctors()
  loadDepts()
  loadPatients()
  loadSchedules()
  loadData()
})
</script>
