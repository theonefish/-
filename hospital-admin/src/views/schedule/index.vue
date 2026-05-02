<template>
  <div class="schedule-page">
    <el-card class="mb-15">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="医生">
          <el-select v-model="searchForm.doctorId" placeholder="请选择医生" clearable filterable>
            <el-option v-for="doc in doctorList" :key="doc.id" :label="doc.name" :value="doc.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
            v-model="searchForm.date"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
          />
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
        <div>
          <el-button type="primary" @click="handleBatchAdd" v-permission="'schedule:create'">
            <el-icon><Plus /></el-icon>批量排班
          </el-button>
          <el-button type="danger" @click="handleBatchDelete" v-permission="'schedule:delete'">
            <el-icon><Delete /></el-icon>批量删除
          </el-button>
        </div>
        <div>
          <el-radio-group v-model="viewMode" size="small">
            <el-radio-button label="list">列表</el-radio-button>
            <el-radio-button label="calendar">日历</el-radio-button>
          </el-radio-group>
        </div>
      </div>

      <el-table
        v-if="viewMode === 'list'"
        :data="tableData"
        v-loading="loading"
        stripe
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="doctorName" label="医生" min-width="100" />
        <el-table-column prop="deptName" label="科室" width="120" />
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="period" label="时段" width="100">
          <template #default="{ row }">
            <el-tag :type="row.period === '上午' ? 'success' : 'warning'">{{ row.period }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quota" label="放号数量" width="100" />
        <el-table-column prop="booked" label="已预约" width="100" />
        <el-table-column prop="status" label="出诊状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              active-text="出诊"
              inactive-text="休息"
              @change="(val: string | number | boolean) => handleStatusChange(row, val as number)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)" v-permission="'schedule:update'">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-permission="'schedule:delete'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-else class="calendar-view">
        <el-calendar v-model="calendarDate">
          <template #date-cell="{ data }">
            <div class="calendar-cell">
              <div class="calendar-date">{{ data.day.split('-')[2] }}</div>
              <div v-for="s in getSchedulesByDate(data.day)" :key="s.id" class="schedule-item">
                <el-tag size="small" :type="s.status === 1 ? 'success' : 'info'">
                  {{ s.doctorName }} {{ s.period }}
                </el-tag>
              </div>
            </div>
          </template>
        </el-calendar>
      </div>

      <div class="pagination" v-if="viewMode === 'list'">
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
        <el-form-item label="医生" prop="doctorId">
          <el-select v-model="form.doctorId" placeholder="请选择医生" style="width: 100%;" filterable>
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
          />
        </el-form-item>
        <el-form-item label="时段" prop="period">
          <el-radio-group v-model="form.period">
            <el-radio-button label="上午" />
            <el-radio-button label="下午" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="放号数量" prop="quota">
          <el-input-number v-model="form.quota" :min="1" :max="100" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="batchDialogVisible" title="批量排班" width="700px">
      <el-form :model="batchForm" :rules="batchRules" ref="batchFormRef" label-width="100px">
        <el-form-item label="医生" prop="doctorId">
          <el-select v-model="batchForm.doctorId" placeholder="请选择医生" style="width: 100%;" filterable>
            <el-option v-for="doc in doctorList" :key="doc.id" :label="doc.name" :value="doc.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围" prop="dateRange">
          <el-date-picker
            v-model="batchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="时段" prop="periods">
          <el-checkbox-group v-model="batchForm.periods">
            <el-checkbox label="上午" />
            <el-checkbox label="下午" />
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="放号数量" prop="quota">
          <el-input-number v-model="batchForm.quota" :min="1" :max="100" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchSubmit" :loading="batchSubmitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getScheduleList, createSchedule, updateSchedule, deleteSchedule,
  batchCreateSchedule, batchDeleteSchedule
} from '@/api/schedule'
import { getDoctorList } from '@/api/doctor'
import type { Schedule } from '@/types'

const loading = ref(false)
const tableData = ref<Schedule[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const doctorList = ref<{ id: number; name: string }[]>([])
const viewMode = ref('list')
const calendarDate = ref(new Date())
const selectedIds = ref<number[]>([])

const searchForm = reactive({
  doctorId: undefined as number | undefined,
  date: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()
const isEdit = ref(false)
const currentId = ref(0)

const form = reactive({
  doctorId: undefined as number | undefined,
  date: '',
  period: '上午',
  quota: 20,
  status: 1
})

const rules = {
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
  date: [{ required: true, message: '请选择日期', trigger: 'change' }],
  period: [{ required: true, message: '请选择时段', trigger: 'change' }]
}

const batchDialogVisible = ref(false)
const batchSubmitLoading = ref(false)
const batchFormRef = ref()
const batchForm = reactive({
  doctorId: undefined as number | undefined,
  dateRange: [] as string[],
  periods: [] as string[],
  quota: 20
})

const batchRules = {
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
  dateRange: [{ required: true, message: '请选择日期范围', trigger: 'change' }],
  periods: [{ required: true, message: '请选择时段', trigger: 'change' }]
}

const loadDoctors = async () => {
  try {
    const res = await getDoctorList({ page: 1, pageSize: 100 }) as any
    doctorList.value = res.list.map((d: any) => ({ id: d.id, name: d.name }))
  } catch {
    doctorList.value = [
      { id: 1, name: '张医生' },
      { id: 2, name: '李医生' }
    ]
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getScheduleList({
      page: page.value,
      pageSize: pageSize.value,
      ...searchForm
    }) as any
    tableData.value = res.list
    total.value = res.total
  } catch {
    tableData.value = [
      { id: 1, doctorId: 1, doctorName: '张医生', deptName: '内科', date: '2024-05-01', period: '上午', quota: 20, booked: 5, status: 1 },
      { id: 2, doctorId: 1, doctorName: '张医生', deptName: '内科', date: '2024-05-01', period: '下午', quota: 20, booked: 3, status: 1 },
      { id: 3, doctorId: 2, doctorName: '李医生', deptName: '外科', date: '2024-05-02', period: '上午', quota: 15, booked: 0, status: 1 }
    ] as any
    total.value = 3
  } finally {
    loading.value = false
  }
}

const getSchedulesByDate = (date: string) => {
  return tableData.value.filter(s => s.date === date)
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const resetSearch = () => {
  searchForm.doctorId = undefined
  searchForm.date = ''
  handleSearch()
}

const handleSelectionChange = (selection: Schedule[]) => {
  selectedIds.value = selection.map(s => s.id)
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增排班'
  Object.assign(form, {
    doctorId: undefined,
    date: '',
    period: '上午',
    quota: 20,
    status: 1
  })
  dialogVisible.value = true
}

const handleBatchAdd = () => {
  Object.assign(batchForm, {
    doctorId: undefined,
    dateRange: [],
    periods: [],
    quota: 20
  })
  batchDialogVisible.value = true
}

const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的排班')
    return
  }
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条排班吗？`, '提示', { type: 'warning' })
    await batchDeleteSchedule(selectedIds.value)
    ElMessage.success('批量删除成功')
    loadData()
  } catch {
    // cancel
  }
}

const handleEdit = (row: Schedule) => {
  isEdit.value = true
  currentId.value = row.id
  dialogTitle.value = '编辑排班'
  Object.assign(form, {
    doctorId: row.doctorId,
    date: row.date,
    period: row.period,
    quota: row.quota,
    status: row.status
  })
  dialogVisible.value = true
}

const handleDelete = async (row: Schedule) => {
  try {
    await ElMessageBox.confirm(`确定删除该排班吗？`, '提示', { type: 'warning' })
    await deleteSchedule(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // cancel
  }
}

const handleStatusChange = async (row: Schedule, val: number) => {
  try {
    await updateSchedule(row.id, { status: val })
    ElMessage.success('状态更新成功')
  } catch {
    row.status = val === 1 ? 0 : 1
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    const doc = doctorList.value.find(d => d.id === form.doctorId)
    const data = { ...form, doctorName: doc?.name }
    if (isEdit.value) {
      await updateSchedule(currentId.value, data)
    } else {
      await createSchedule(data)
    }
    ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
    dialogVisible.value = false
    loadData()
  } finally {
    submitLoading.value = false
  }
}

const handleBatchSubmit = async () => {
  await batchFormRef.value.validate()
  batchSubmitLoading.value = true
  try {
    const [startDate, endDate] = batchForm.dateRange
    const list: any[] = []
    const start = new Date(startDate)
    const end = new Date(endDate)
    for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
      const dateStr = d.toISOString().split('T')[0]
      batchForm.periods.forEach(period => {
        list.push({
          doctorId: batchForm.doctorId,
          date: dateStr,
          period,
          quota: batchForm.quota,
          status: 1
        })
      })
    }
    await batchCreateSchedule(list)
    ElMessage.success('批量排班成功')
    batchDialogVisible.value = false
    loadData()
  } finally {
    batchSubmitLoading.value = false
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
  loadData()
})
</script>

<style scoped lang="scss">
.calendar-view {
  .calendar-cell {
    min-height: 80px;

    .calendar-date {
      font-weight: bold;
      margin-bottom: 4px;
    }

    .schedule-item {
      margin-bottom: 2px;
    }
  }
}
</style>
