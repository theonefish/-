<template>
  <div class="statistics-page">
    <el-row :gutter="15" class="mb-15">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="chart-header">
              <span>预约趋势</span>
              <el-radio-group v-model="trendPeriod" size="small" @change="loadTrendData">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">本年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="trendChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="chart-header">
              <span>科室预约占比</span>
              <el-date-picker
                v-model="pieDateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                size="small"
                value-format="YYYY-MM-DD"
                @change="loadPieData"
              />
            </div>
          </template>
          <div ref="pieChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="15">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="chart-header">
              <span>医生接诊量排行</span>
              <el-radio-group v-model="rankPeriod" size="small" @change="loadRankingData">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">本年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="rankChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { getAppointmentTrend, getDeptAppointmentRatio, getDoctorRanking } from '@/api/statistics'

const trendChartRef = ref<HTMLElement>()
const pieChartRef = ref<HTMLElement>()
const rankChartRef = ref<HTMLElement>()
let trendChart: echarts.ECharts | null = null
let pieChart: echarts.ECharts | null = null
let rankChart: echarts.ECharts | null = null

const trendPeriod = ref('week')
const pieDateRange = ref<string[]>([])
const rankPeriod = ref('month')

const initTrendChart = (data: any) => {
  if (!trendChartRef.value) return
  trendChart = echarts.init(trendChartRef.value)
  trendChart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['预约量', '就诊量', '取消量'] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: data?.dates || ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: { type: 'value' },
    series: [
      {
        name: '预约量',
        type: 'line',
        smooth: true,
        data: data?.appointments || [120, 132, 101, 134, 90, 230, 210],
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
        data: data?.visits || [100, 120, 90, 110, 80, 180, 170]
      },
      {
        name: '取消量',
        type: 'line',
        smooth: true,
        data: data?.cancels || [10, 5, 8, 12, 6, 15, 20]
      }
    ]
  })
}

const initPieChart = (data: any) => {
  if (!pieChartRef.value) return
  pieChart = echarts.init(pieChartRef.value)
  pieChart.setOption({
    tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [
      {
        name: '科室预约',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: { show: false, position: 'center' },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: { show: false },
        data: data || [
          { value: 335, name: '内科' },
          { value: 310, name: '外科' },
          { value: 234, name: '儿科' },
          { value: 135, name: '骨科' },
          { value: 148, name: '眼科' },
          { value: 120, name: '妇产科' }
        ]
      }
    ]
  })
}

const initRankChart = (data: any) => {
  if (!rankChartRef.value) return
  rankChart = echarts.init(rankChartRef.value)
  rankChart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'value' },
    yAxis: {
      type: 'category',
      data: data?.names || ['张医生', '李医生', '王医生', '刘医生', '陈医生', '赵医生', '周医生', '吴医生'],
      inverse: true
    },
    series: [
      {
        name: '接诊量',
        type: 'bar',
        data: data?.counts || [320, 302, 281, 254, 240, 230, 220, 210],
        itemStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 1, y2: 0,
            colorStops: [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ]
          }
        }
      }
    ]
  })
}

const loadTrendData = async () => {
  try {
    const res = await getAppointmentTrend({ period: trendPeriod.value })
    initTrendChart(res)
  } catch {
    initTrendChart(null)
  }
}

const loadPieData = async () => {
  try {
    const res = await getDeptAppointmentRatio({ dateRange: pieDateRange.value })
    initPieChart(res)
  } catch {
    initPieChart(null)
  }
}

const loadRankingData = async () => {
  try {
    const res = await getDoctorRanking({ period: rankPeriod.value })
    initRankChart(res)
  } catch {
    initRankChart(null)
  }
}

onMounted(() => {
  loadTrendData()
  loadPieData()
  loadRankingData()
  window.addEventListener('resize', () => {
    trendChart?.resize()
    pieChart?.resize()
    rankChart?.resize()
  })
})

onUnmounted(() => {
  trendChart?.dispose()
  pieChart?.dispose()
  rankChart?.dispose()
})
</script>

<style scoped lang="scss">
.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
