<template>
  <div class="ai-page">
    <el-card class="mb-15">
      <template #header>
        <div class="ai-header">
          <span><el-icon><Cpu /></el-icon> AI 报告分析</span>
          <el-tag type="success">通义千问</el-tag>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card shadow="never">
            <template #header>选择报告</template>
            <el-form :model="aiForm" label-width="80px">
              <el-form-item label="报告类型">
                <el-radio-group v-model="aiForm.reportType">
                  <el-radio-button label="exam">检查报告</el-radio-button>
                  <el-radio-button label="lab">检验报告</el-radio-button>
                  <el-radio-button label="visit">就诊记录</el-radio-button>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="患者">
                <el-select v-model="aiForm.patientId" placeholder="请选择患者" filterable style="width: 100%;">
                  <el-option v-for="p in patientList" :key="p.id" :label="p.name" :value="p.id" />
                </el-select>
              </el-form-item>
              <el-form-item label="报告">
                <el-select v-model="aiForm.reportId" placeholder="请选择报告" style="width: 100%;">
                  <el-option v-for="r in reportList" :key="r.id" :label="r.title" :value="r.id" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleAnalyze" :loading="analyzing">
                  <el-icon><MagicStick /></el-icon> AI 分析
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
        <el-col :span="16">
          <el-card shadow="never">
            <template #header>分析结果</template>
            <div v-if="analysisResult" class="analysis-result">
              <el-alert
                :title="analysisResult.summary"
                :type="analysisResult.riskLevel"
                :closable="false"
                show-icon
                class="mb-15"
              />
              <el-divider content-position="left">详细解读</el-divider>
              <div class="analysis-content" v-html="analysisResult.detail"></div>
              <el-divider content-position="left">建议</el-divider>
              <el-timeline>
                <el-timeline-item
                  v-for="(suggestion, index) in analysisResult.suggestions"
                  :key="index"
                  :type="suggestion.type"
                  :icon="suggestion.icon"
                >
                  {{ suggestion.content }}
                </el-timeline-item>
              </el-timeline>
            </div>
            <el-empty v-else description="请选择报告并点击 AI 分析" />
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserList } from '@/api/user'
import { getExamList } from '@/api/exam'
import { getLabList } from '@/api/lab'
import { getVisitList } from '@/api/visit'

const patientList = ref<{ id: number; name: string }[]>([])
const reportList = ref<{ id: number; title: string }[]>([])
const analyzing = ref(false)
const analysisResult = ref<any>(null)

const aiForm = reactive({
  reportType: 'exam',
  patientId: undefined as number | undefined,
  reportId: undefined as number | undefined
})

const loadPatients = async () => {
  try {
    const res = await getUserList({ page: 1, pageSize: 100 }) as any
    patientList.value = res.list.map((p: any) => ({ id: p.id, name: p.name }))
  } catch {
    patientList.value = [{ id: 1, name: '张三' }, { id: 2, name: '李四' }]
  }
}

const loadReports = async () => {
  if (!aiForm.patientId) return
  try {
    let res: any
    switch (aiForm.reportType) {
      case 'exam':
        res = await getExamList({ patientId: aiForm.patientId, page: 1, pageSize: 100 })
        break
      case 'lab':
        res = await getLabList({ patientId: aiForm.patientId, page: 1, pageSize: 100 })
        break
      case 'visit':
        res = await getVisitList({ patientId: aiForm.patientId, page: 1, pageSize: 100 })
        break
    }
    reportList.value = res?.list?.map((item: any) => ({
      id: item.id,
      title: item.examType || item.labType || item.diagnosis || `报告 #${item.id}`
    })) || []
  } catch {
    reportList.value = []
  }
}

const handleAnalyze = async () => {
  if (!aiForm.reportId) {
    ElMessage.warning('请选择报告')
    return
  }
  analyzing.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1500))
    analysisResult.value = {
      summary: '分析完成，各项指标基本正常',
      riskLevel: 'success',
      detail: `
        <p><strong>血常规检查：</strong></p>
        <p>白细胞计数 (WBC): 6.5 × 10^9/L (参考范围: 4.0-10.0) - <span style="color: #67c23a;">正常</span></p>
        <p>红细胞计数 (RBC): 4.8 × 10^12/L (参考范围: 4.0-5.5) - <span style="color: #67c23a;">正常</span></p>
        <p>血红蛋白 (Hb): 145 g/L (参考范围: 120-160) - <span style="color: #67c23a;">正常</span></p>
        <p>血小板计数 (PLT): 220 × 10^9/L (参考范围: 100-300) - <span style="color: #67c23a;">正常</span></p>
      `,
      suggestions: [
        { type: 'primary', icon: 'Check', content: '各项指标均在正常范围内，身体状况良好' },
        { type: 'success', icon: 'Food', content: '建议保持均衡饮食，多摄入新鲜蔬果' },
        { type: 'warning', icon: 'AlarmClock', content: '建议每年进行一次全面体检' },
        { type: 'info', icon: 'Basketball', content: '适当增加运动量，保持每周3次有氧运动' }
      ]
    }
  } finally {
    analyzing.value = false
  }
}

watch(() => aiForm.patientId, loadReports)
watch(() => aiForm.reportType, loadReports)

onMounted(() => {
  loadPatients()
})
</script>

<style scoped lang="scss">
.ai-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .el-icon {
    vertical-align: middle;
    margin-right: 8px;
  }
}

.analysis-result {
  .analysis-content {
    line-height: 2;
    color: var(--text-regular);
  }
}
</style>
