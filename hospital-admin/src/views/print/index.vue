<template>
  <div class="print-page">
    <el-card>
      <template #header>
        <span>报告打印中心</span>
      </template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="检查报告" name="exam">
          <el-table :data="examList" stripe border>
            <el-table-column prop="patientName" label="患者" />
            <el-table-column prop="examType" label="检查类型" />
            <el-table-column prop="doctorName" label="医生" />
            <el-table-column prop="createTime" label="检查时间">
              <template #default="{ row }">{{ formatDate(row.createTime) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button link type="primary" @click="handlePrintExam(row)">打印</el-button>
                <el-button link type="success" @click="handlePreviewExam(row)">预览</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="检验报告" name="lab">
          <el-table :data="labList" stripe border>
            <el-table-column prop="patientName" label="患者" />
            <el-table-column prop="labType" label="检验类型" />
            <el-table-column prop="doctorName" label="医生" />
            <el-table-column prop="createTime" label="检验时间">
              <template #default="{ row }">{{ formatDate(row.createTime) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button link type="primary" @click="handlePrintLab(row)">打印</el-button>
                <el-button link type="success" @click="handlePreviewLab(row)">预览</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="医嘱报告" name="advice">
          <el-table :data="visitList" stripe border>
            <el-table-column prop="patientName" label="患者" />
            <el-table-column prop="doctorName" label="医生" />
            <el-table-column prop="diagnosis" label="诊断" show-overflow-tooltip />
            <el-table-column prop="visitTime" label="就诊时间">
              <template #default="{ row }">{{ formatDate(row.visitTime) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button link type="primary" @click="handlePrintVisit(row)">打印</el-button>
                <el-button link type="success" @click="handlePreviewVisit(row)">预览</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog v-model="previewVisible" title="报告预览" width="800px">
      <div id="print-area" class="print-area">
        <div class="print-header">
          <h2>XX 医院</h2>
          <h3>{{ previewTitle }}</h3>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="患者姓名">{{ previewData?.patientName }}</el-descriptions-item>
          <el-descriptions-item label="性别">男</el-descriptions-item>
          <el-descriptions-item label="年龄">34</el-descriptions-item>
          <el-descriptions-item label="报告日期">{{ formatDate(new Date()) }}</el-descriptions-item>
        </el-descriptions>
        <div class="print-content">
          <h4>报告内容</h4>
          <p v-if="previewData?.result">{{ previewData.result }}</p>
          <p v-if="previewData?.diagnosis"><strong>诊断：</strong>{{ previewData.diagnosis }}</p>
          <p v-if="previewData?.advice"><strong>医嘱：</strong>{{ previewData.advice }}</p>
        </div>
        <div class="print-footer">
          <p>医生签名：__________</p>
          <p>报告日期：{{ formatDate(new Date()) }}</p>
        </div>
      </div>
      <template #footer>
        <el-button type="primary" @click="handlePrint">打印</el-button>
        <el-button @click="previewVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import printJS from 'print-js'
import { getExamList } from '@/api/exam'
import { getLabList } from '@/api/lab'
import { getVisitList } from '@/api/visit'
import { formatDate } from '@/utils/format'

const activeTab = ref('exam')
const examList = ref<any[]>([])
const labList = ref<any[]>([])
const visitList = ref<any[]>([])

const previewVisible = ref(false)
const previewTitle = ref('')
const previewData = ref<any>(null)

const loadData = async () => {
  try {
    const [examRes, labRes, visitRes] = await Promise.all([
      getExamList({ page: 1, pageSize: 10 }),
      getLabList({ page: 1, pageSize: 10 }),
      getVisitList({ page: 1, pageSize: 10 })
    ]) as any
    examList.value = examRes.list || []
    labList.value = labRes.list || []
    visitList.value = visitRes.list || []
  } catch {
    examList.value = [
      { id: 1, patientName: '张三', examType: 'X光', doctorName: '张医生', result: '胸部X光未见明显异常', createTime: '2024-05-01' }
    ]
    labList.value = [
      { id: 1, patientName: '李四', labType: '血常规', doctorName: '李医生', result: '各项指标正常', createTime: '2024-05-01' }
    ]
    visitList.value = [
      { id: 1, patientName: '张三', doctorName: '张医生', diagnosis: '上呼吸道感染', advice: '多休息，多喝水', visitTime: '2024-05-01' }
    ]
  }
}

const handlePrintExam = (row: any) => {
  previewData.value = row
  previewTitle.value = '检查报告'
  setTimeout(() => {
    printJS({ printable: 'print-area', type: 'html', targetStyles: ['*'] })
  }, 100)
}

const handlePreviewExam = (row: any) => {
  previewData.value = row
  previewTitle.value = '检查报告'
  previewVisible.value = true
}

const handlePrintLab = (row: any) => {
  previewData.value = row
  previewTitle.value = '检验报告'
  setTimeout(() => {
    printJS({ printable: 'print-area', type: 'html', targetStyles: ['*'] })
  }, 100)
}

const handlePreviewLab = (row: any) => {
  previewData.value = row
  previewTitle.value = '检验报告'
  previewVisible.value = true
}

const handlePrintVisit = (row: any) => {
  previewData.value = row
  previewTitle.value = '医嘱报告'
  setTimeout(() => {
    printJS({ printable: 'print-area', type: 'html', targetStyles: ['*'] })
  }, 100)
}

const handlePreviewVisit = (row: any) => {
  previewData.value = row
  previewTitle.value = '医嘱报告'
  previewVisible.value = true
}

const handlePrint = () => {
  printJS({ printable: 'print-area', type: 'html', targetStyles: ['*'] })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.print-area {
  padding: 20px;
  background: #fff;

  .print-header {
    text-align: center;
    margin-bottom: 20px;

    h2 {
      font-size: 24px;
      margin-bottom: 5px;
    }

    h3 {
      font-size: 18px;
      color: #666;
    }
  }

  .print-content {
    margin: 20px 0;
    min-height: 200px;

    h4 {
      border-bottom: 1px solid #ddd;
      padding-bottom: 10px;
      margin-bottom: 15px;
    }

    p {
      line-height: 2;
    }
  }

  .print-footer {
    display: flex;
    justify-content: space-between;
    margin-top: 40px;
    padding-top: 20px;
    border-top: 1px solid #ddd;
  }
}
</style>
