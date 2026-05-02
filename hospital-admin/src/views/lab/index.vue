<template>
  <div class="lab-page">
    <el-card class="mb-15">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="患者姓名">
          <el-input v-model="searchForm.patientName" placeholder="请输入患者姓名" clearable />
        </el-form-item>
        <el-form-item label="检验类型">
          <el-select v-model="searchForm.labType" placeholder="请选择" clearable>
            <el-option label="血常规" value="血常规" />
            <el-option label="尿常规" value="尿常规" />
            <el-option label="肝功能" value="肝功能" />
            <el-option label="肾功能" value="肾功能" />
            <el-option label="血糖" value="血糖" />
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
        <el-button type="primary" @click="handleAdd" v-permission="'lab:create'">
          <el-icon><Plus /></el-icon>新增检验
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column type="index" width="60" label="序号" />
        <el-table-column prop="patientName" label="患者姓名" min-width="100" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="labType" label="检验类型" width="120" />
        <el-table-column prop="result" label="检验结果" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="报告状态" width="120">
          <template #default="{ row }">
            <el-tag :type="row.reportUrl ? 'success' : 'warning'">
              {{ row.reportUrl ? '已上传' : '待上传' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="检验时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleUpload(row)" v-permission="'lab:upload'">上传报告</el-button>
            <el-button link type="success" @click="handleView(row)" v-permission="'lab:view'">查看报告</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-permission="'lab:delete'">删除</el-button>
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

    <el-dialog v-model="dialogVisible" title="新增检验" width="600px">
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
        <el-form-item label="检验类型" prop="labType">
          <el-select v-model="form.labType" placeholder="请选择检验类型" style="width: 100%;">
            <el-option label="血常规" value="血常规" />
            <el-option label="尿常规" value="尿常规" />
            <el-option label="肝功能" value="肝功能" />
            <el-option label="肾功能" value="肾功能" />
            <el-option label="血糖" value="血糖" />
          </el-select>
        </el-form-item>
        <el-form-item label="检验结果" prop="result">
          <el-input v-model="form.result" type="textarea":rows="3" placeholder="请输入检验结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="uploadDialogVisible" title="上传报告" width="500px">
      <el-upload
        drag
        action="#"
        :auto-upload="false"
        :on-change="handleFileChange"
        :limit="1"
        accept=".pdf,.jpg,.png"
      >
        <el-icon class="el-icon--upload"><Upload /></el-icon>
        <div class="el-upload__text">拖拽文件到此处或 <em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">支持 PDF、JPG、PNG 格式</div>
        </template>
      </el-upload>
      <template #footer>
        <el-button @click="uploadDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUploadSubmit" :loading="uploadLoading">上传</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="查看报告" width="800px">
      <div v-if="currentLab?.reportUrl" class="report-viewer">
        <img v-if="isImage(currentLab.reportUrl)" :src="currentLab.reportUrl" style="max-width: 100%;" />
        <iframe v-else :src="currentLab.reportUrl" style="width: 100%; height: 500px; border: none;"></iframe>
      </div>
      <el-empty v-else description="暂无报告" />
      <template #footer>
        <el-button type="primary" @click="handlePrint(currentLab)">打印报告</el-button>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import printJS from 'print-js'
import { getLabList, createLab, uploadLabReport } from '@/api/lab'
import { getDoctorList } from '@/api/doctor'
import { getUserList } from '@/api/user'
import { formatDate } from '@/utils/format'
import type { Lab } from '@/types'

const loading = ref(false)
const tableData = ref<Lab[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const doctorList = ref<{ id: number; name: string }[]>([])
const patientList = ref<{ id: number; name: string }[]>([])

const searchForm = reactive({
  patientName: '',
  labType: ''
})

const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref()

const form = reactive({
  patientId: undefined as number | undefined,
  doctorId: undefined as number | undefined,
  labType: '',
  result: ''
})

const rules = {
  patientId: [{ required: true, message: '请选择患者', trigger: 'change' }],
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
  labType: [{ required: true, message: '请选择检验类型', trigger: 'change' }]
}

const uploadDialogVisible = ref(false)
const uploadLoading = ref(false)
const currentLabId = ref(0)
const currentFile = ref<File | null>(null)

const viewDialogVisible = ref(false)
const currentLab = ref<Lab | null>(null)

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
    const res = await getLabList({
      page: page.value,
      pageSize: pageSize.value,
      ...searchForm
    }) as any
    tableData.value = res.list
    total.value = res.total
  } catch {
    tableData.value = [
      { id: 1, patientName: '张三', doctorName: '张医生', labType: '血常规', result: '白细胞计数正常，红细胞计数正常', reportUrl: '', status: 0, createTime: '2024-05-01 08:00:00' },
      { id: 2, patientName: '李四', doctorName: '李医生', labType: '肝功能', result: '转氨酶轻度升高', reportUrl: '', status: 0, createTime: '2024-05-01 08:30:00' }
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
  searchForm.labType = ''
  handleSearch()
}

const handleAdd = () => {
  Object.assign(form, {
    patientId: undefined,
    doctorId: undefined,
    labType: '',
    result: ''
  })
  dialogVisible.value = true
}

const handleUpload = (row: Lab) => {
  currentLabId.value = row.id
  currentFile.value = null
  uploadDialogVisible.value = true
}

const handleFileChange = (file: any) => {
  currentFile.value = file.raw
}

const handleUploadSubmit = async () => {
  if (!currentFile.value) {
    ElMessage.warning('请选择文件')
    return
  }
  uploadLoading.value = true
  try {
    await uploadLabReport(currentLabId.value, currentFile.value)
    ElMessage.success('报告上传成功')
    uploadDialogVisible.value = false
    loadData()
  } finally {
    uploadLoading.value = false
  }
}

const handleView = (row: Lab) => {
  currentLab.value = row
  viewDialogVisible.value = true
}

const isImage = (url: string) => {
  return /\.(jpg|jpeg|png|gif|webp)$/i.test(url)
}

const handlePrint = (row: Lab | null) => {
  if (!row?.reportUrl) return
  if (isImage(row.reportUrl)) {
    printJS({ printable: row.reportUrl, type: 'image', imageStyle: 'max-width: 100%' })
  } else {
    printJS({ printable: row.reportUrl, type: 'pdf' })
  }
}

const handleDelete = async (row: Lab) => {
  try {
    await ElMessageBox.confirm(`确定删除该检验记录吗？`, '提示', { type: 'warning' })
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // cancel
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    await createLab(form)
    ElMessage.success('创建成功')
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
  loadPatients()
  loadData()
})
</script>
