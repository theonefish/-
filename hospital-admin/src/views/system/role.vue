<template>
  <div class="role-page">
    <el-card class="mb-15">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.keyword" placeholder="请输入角色名称" clearable />
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
        <el-button type="primary" @click="handleAdd" v-permission="'system:role:create'">
          <el-icon><Plus /></el-icon>新增角色
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column type="index" width="60" label="序号" />
        <el-table-column prop="name" label="角色名称" min-width="120" />
        <el-table-column prop="code" label="角色编码" width="150" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
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
            <el-button link type="primary" @click="handleEdit(row)" v-permission="'system:role:update'">编辑</el-button>
            <el-button link type="success" @click="handleAssignMenu(row)" v-permission="'system:role:assign'">分配权限</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-permission="'system:role:delete'">删除</el-button>
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
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea":rows="3" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="menuDialogVisible" title="分配权限" width="500px">
      <el-tree
        ref="menuTreeRef"
        :data="menuTree"
        show-checkbox
        node-key="id"
        :props="{ label: 'name', children: 'children' }"
        :default-checked-keys="checkedMenus"
      />
      <template #footer>
        <el-button @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleMenuSubmit" :loading="menuSubmitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoleList, createRole, updateRole, deleteRole } from '@/api/system'
import { getMenuList } from '@/api/system'
import type { Role, MenuItem } from '@/types'

const loading = ref(false)
const tableData = ref<Role[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  keyword: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()
const isEdit = ref(false)
const currentId = ref(0)

const form = reactive({
  name: '',
  code: '',
  description: '',
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

const menuDialogVisible = ref(false)
const menuSubmitLoading = ref(false)
const menuTreeRef = ref()
const menuTree = ref<MenuItem[]>([])
const checkedMenus = ref<number[]>([])
const currentRoleId = ref(0)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getRoleList({
      page: page.value,
      pageSize: pageSize.value,
      ...searchForm
    }) as any
    tableData.value = res.list
    total.value = res.total
  } catch {
    tableData.value = [
      { id: 1, name: '超级管理员', code: 'super_admin', description: '系统超级管理员', menuIds: [], status: 1 },
      { id: 2, name: '医生', code: 'doctor', description: '医生角色', menuIds: [], status: 1 },
      { id: 3, name: '护士', code: 'nurse', description: '护士角色', menuIds: [], status: 1 }
    ] as any
    total.value = 3
  } finally {
    loading.value = false
  }
}

const loadMenus = async () => {
  try {
    const res = await getMenuList() as any
    menuTree.value = buildTree(res)
  } catch {
    menuTree.value = []
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

const handleSearch = () => {
  page.value = 1
  loadData()
}

const resetSearch = () => {
  searchForm.keyword = ''
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增角色'
  Object.assign(form, {
    name: '',
    code: '',
    description: '',
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row: Role) => {
  isEdit.value = true
  currentId.value = row.id
  dialogTitle.value = '编辑角色'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row: Role) => {
  try {
    await ElMessageBox.confirm(`确定删除角色"${row.name}"吗？`, '提示', { type: 'warning' })
    await deleteRole(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // cancel
  }
}

const handleStatusChange = async (row: Role, val: number) => {
  try {
    await updateRole(row.id, { status: val })
    ElMessage.success('状态更新成功')
  } catch {
    row.status = val === 1 ? 0 : 1
  }
}

const handleAssignMenu = (row: Role) => {
  currentRoleId.value = row.id
  checkedMenus.value = row.menuIds || []
  menuDialogVisible.value = true
}

const handleMenuSubmit = async () => {
  const checkedKeys = menuTreeRef.value.getCheckedKeys()
  const halfCheckedKeys = menuTreeRef.value.getHalfCheckedKeys()
  const menuIds = [...checkedKeys, ...halfCheckedKeys]
  menuSubmitLoading.value = true
  try {
    await updateRole(currentRoleId.value, { menuIds })
    ElMessage.success('权限分配成功')
    menuDialogVisible.value = false
    loadData()
  } finally {
    menuSubmitLoading.value = false
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateRole(currentId.value, form)
    } else {
      await createRole(form)
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
  loadData()
  loadMenus()
})
</script>
