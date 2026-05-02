<template>
  <div class="dashboard">
    <el-row :gutter="15" class="mb-15">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #e6f7ff; color: #1890ff;">
              <el-icon size="32"><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.doctorCount }}</div>
              <div class="stat-label">医生总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #f6ffed; color: #52c41a;">
              <el-icon size="32"><OfficeBuilding /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.deptCount }}</div>
              <div class="stat-label">科室数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #fff7e6; color: #fa8c16;">
              <el-icon size="32"><Timer /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.appointmentCount }}</div>
              <div class="stat-label">今日预约</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #fff1f0; color: #f5222d;">
              <el-icon size="32"><FirstAidKit /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.visitCount }}</div>
              <div class="stat-label">今日就诊</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="15" class="mb-15">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <span>预约趋势</span>
          </template>
          <div ref="trendChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span>科室预约占比</span>
          </template>
          <div ref="pieChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="15">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>最新公告</span>
            <el-button text type="primary" @click="$router.push('/system/notice')">更多</el-button>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="notice in notices"
              :key="notice.id"
              :timestamp="formatDate(notice.createTime)"
            >
              {{ notice.title }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>医生接诊量排行</span>
          </template>
          <el-table :data="doctorRanking" stripe>
            <el-table-column type="index" width="50" />
            <el-table-column prop="name" label="医生" />
            <el-table-column prop="deptName" label="科室" />
            <el-table-column prop="count" label="接诊量" sortable />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { formatDate } from '@/utils/format'
import { getDashboardStats } from '@/api/statistics'

const trendChartRef = ref<HTMLElement>()
const pieChartRef = ref<HTMLElement>()
let trendChart: echarts.ECharts | null = null
let pieChart: echarts.ECharts | null = null

const stats = ref({
  doctorCount: 0,
  deptCount: 0,
  appointmentCount: 0,
  visitCount: 0
})

const notices = ref([
  { id: 1, title: '关于五一劳动节放假安排的通知', createTime: '2024-04-25 10:00:00' },
  { id: 2, title: '系统维护公告', createTime: '2024-04-20 09:00:00' },
  { id: 3, title: '新科室开设通知', createTime: '2024-04-15 14:00:00' }
])

const doctorRanking = ref([
  { name: '张医生', deptName: '内科', count: 156 },
  { name: '李医生', deptName: '外科', count: 142 },
  { name: '王医生', deptName: '儿科', count: 128 },
  { name: '刘医生', deptName: '骨科', count: 115 },
  { name: '陈医生', deptName: '眼科', count: 98 }
])

const initTrendChart = () => {
  if (!trendChartRef.value) return
  trendChart = echarts.init(trendChartRef.value)
  trendChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: { type: 'value' },
    series: [
      {
        name: '预约量',
        type: 'line',
        smooth: true,
        data: [120, 132, 101, 134, 90, 230, 210],
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(64,158,255,0.3)' },
              { offset: 1, color: 'rgba(64,158,255,0.05)' }
            ]
          }
        }
      },
      {
        name: '就诊量',
        type: 'line',
        smooth: true,
        data: [100, 120, 90, 110, 80, 180, 170]
      }
    ]
  })
}

const initPieChart = () => {
  if (!pieChartRef.value) return
  pieChart = echarts.init(pieChartRef.value)
  pieChart.setOption({
    tooltip: { trigger: 'item' },
    legend: { orient: 'vertical', left: 'left' },
    series: [
      {
        type: 'pie',
        radius: '60%',
        data: [
          { value: 335, name: '内科' },
          { value: 310, name: '外科' },
          { value: 234, name: '儿科' },
          { value: 135, name: '骨科' },
          { value: 148, name: '眼科' }
        ]
      }
    ]
  })
}

const loadStats = async () => {
  try {
    const res = await getDashboardStats() as any
    stats.value = res
  } catch {
    stats.value = {
      doctorCount: 48,
      deptCount: 16,
      appointmentCount: 126,
      visitCount: 98
    }
  }
}

onMounted(() => {
  initTrendChart()
  initPieChart()
  loadStats()
  window.addEventListener('resize', () => {
    trendChart?.resize()
    pieChart?.resize()
  })
})

onUnmounted(() => {
  trendChart?.dispose()
  pieChart?.dispose()
})
</script>

<style scoped lang="scss">
.stat-card {
  display: flex;
  align-items: center;

  .stat-icon {
    width: 64px;
    height: 64px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
  }

  .stat-value {
    font-size: 28px;
    font-weight: bold;
    color: var(--text-primary);
  }

  .stat-label {
    font-size: 14px;
    color: var(--text-secondary);
    margin-top: 4px;
  }
}

:deep(.el-card__header) {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
