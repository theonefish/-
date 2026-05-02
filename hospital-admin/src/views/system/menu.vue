<template>
  <div class="menu-page">
    <el-card>
      <div class="table-toolbar">
        <el-button type="primary" @click="handleAdd" v-permission="'system:menu:create'">
          <el-icon><Plus /></el-icon>新增菜单
        </el-button>
      </div>

      <el-table
        :data="tableData"
        v-loading="loading"
        stripe
        border
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="name" label="菜单名称" min-width="150" />
        <el-table-column prop="path" label="路由路径" min-width="150" />
        <el-table-column prop="component" label="组件路径" min-width="200" show-overflow-tooltip />
        <el-table-column prop="icon" label="图标" width="80">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="hidden" label="隐藏" width="80">
          <template #default="{ row }">
            <el-tag :type="row.hidden ? 'danger' : 'success'">{{ row.hidden ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleAddChild(row)" v-permission="'system:menu:create'">添加子菜单</el-button>
            <el-button link type="primary" @click="handleEdit(row)" v-permission="'system:menu:update'">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-permission="'system:menu:delete'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="上级菜单">
          <el-tree-select
            v-model="form.parentId"
            :data="menuTree"
            :props="{ label: 'name', value: 'id' } as any"
            placeholder="请选择上级菜单"
            clearable
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="路由路径" prop="path">
          <el-input v-model="form.path" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item label="组件路径">
          <el-input v-model="form.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="form.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="是否隐藏">
          <el-switch v-model="form.hidden" :active-value="true" :inactive-value="false" />
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
import { getMenuList, createMenu, updateMenu, deleteMenu } from '@/api/system'
import type { MenuItem } from '@/types'

const loading = ref(false)
const tableData = ref<MenuItem[]>([])
const menuTree = ref<MenuItem[]>([])

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()
const isEdit = ref(false)
const currentId = ref(0)

const form = reactive({
  parentId: null as number | null,
  name: '',
  path: '',
  component: '',
  icon: '',
  sort: 0,
  hidden: false
})

const rules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  path: [{ required: true, message: '请输入路由路径', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getMenuList() as any
    tableData.value = buildTree(res)
    menuTree.value = [{ id: 0, name: '根菜单', path: '', sort: 0, hidden: false, parentId: null, children: tableData.value }]
  } catch {
    tableData.value = [
      {
        id: 1, name: '系统管理', path: '/system', component: '', icon: 'Setting', sort: 1, hidden: false, parentId: null,
        children: [
          { id: 11, name: '菜单管理', path: '/system/menu', component: 'system/menu', icon: '', sort: 1, hidden: false, parentId: 1 },
          { id: 12, name: '角色管理', path: '/system/role', component: 'system/role', icon: '', sort: 2, hidden: false, parentId: 1 }
        ]
      }
    ]
    menuTree.value = [{ id: 0, name: '根菜单', path: '', sort: 0, hidden: false, parentId: null, children: tableData.value }]
  } finally {
    loading.value = false
  }
}

const buildTree = (data: MenuItem[]): MenuItem[] => {
  const map: Record<number, MenuItem> = {}
  const tree: MenuItem[] = []
  data.forEach(item => {
    map[item.id] = { ...item, children: [] }
  })
  data.forEach(item => {
    if (item.parentId && map[item.parentId]) {
      map[item.parentId].children!.push(map[item.id])
    } else {
      tree.push(map[item.id])
    }
  })
  return tree
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增菜单'
  Object.assign(form, {
    parentId: null,
    name: '',
    path: '',
    component: '',
    icon: '',
    sort: 0,
    hidden: false
  })
  dialogVisible.value = true
}

const handleAddChild = (row: MenuItem) => {
  isEdit.value = false
  dialogTitle.value = '新增子菜单'
  Object.assign(form, {
    parentId: row.id,
    name: '',
    path: '',
    component: '',
    icon: '',
    sort: 0,
    hidden: false
  })
  dialogVisible.value = true
}

const handleEdit = (row: MenuItem) => {
  isEdit.value = true
  currentId.value = row.id
  dialogTitle.value = '编辑菜单'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row: MenuItem) => {
  try {
    await ElMessageBox.confirm(`确定删除菜单"${row.name}"吗？`, '提示', { type: 'warning' })
    await deleteMenu(row.id)
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
    if (isEdit.value) {
      await updateMenu(currentId.value, form)
    } else {
      await createMenu(form)
    }
    ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
    dialogVisible.value = false
    loadData()
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>
