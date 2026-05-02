# 医院预约挂号管理端 - 后端接口需求文档

> 文档版本：v1.0
> 基础路径：`/api`
> 数据格式：JSON
> 认证方式：Bearer Token

---

## 一、通用规范

### 1.1 响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | int | 状态码：200成功，401未登录，403无权限，500错误 |
| message | string | 提示信息 |
| data | any | 业务数据 |

### 1.2 分页参数

所有列表接口统一支持：

| 参数 | 类型 | 说明 | 默认值 |
|------|------|------|--------|
| page | int | 当前页码 | 1 |
| pageSize | int | 每页条数 | 10 |

分页响应统一格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [],
    "total": 100,
    "page": 1,
    "pageSize": 10
  }
}
```

### 1.3 认证头

```
Authorization: Bearer <token>
```

---

## 二、认证模块

### 2.1 用户登录

- **POST** `/auth/login`
- **请求体：**

```json
{
  "username": "admin",
  "password": "123456",
  "captcha": "a3f8"  // 可选
}
```

- **响应：**

```json
{
  "code": 200,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "expires": 7200
  }
}
```

### 2.2 获取当前用户信息

- **GET** `/auth/info`
- **响应：**

```json
{
  "code": 200,
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "avatar": "https://...",
    "role": "超级管理员",
    "roleId": 1,
    "permissions": ["*", "dashboard:view", "department:view"]
  }
}
```

### 2.3 获取用户菜单

- **GET** `/auth/menus`
- **响应：**

```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "Dashboard",
      "path": "/dashboard",
      "component": "dashboard/index",
      "icon": "HomeFilled",
      "parentId": null,
      "sort": 1,
      "hidden": false
    }
  ]
}
```

### 2.4 修改密码

- **POST** `/auth/password`
- **请求体：**

```json
{
  "oldPassword": "123456",
  "newPassword": "abcdef"
}
```

---

## 三、科室管理

### 3.1 科室列表

- **GET** `/department/list`
- **查询参数：** `keyword`, `status`, `page`, `pageSize`
- **响应数据字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 科室ID |
| name | string | 科室名称 |
| description | string | 科室简介 |
| phone | string | 科室电话 |
| recommended | boolean | 是否首页推荐 |
| status | int | 0禁用 1启用 |
| sort | int | 排序号 |

### 3.2 新增科室

- **POST** `/department`
- **请求体：** 同上（不含id）

### 3.3 编辑科室

- **PUT** `/department/{id}`

### 3.4 删除科室

- **DELETE** `/department/{id}`

---

## 四、医生管理

### 4.1 医生列表

- **GET** `/doctor/list`
- **查询参数：** `keyword`, `deptId`, `status`, `page`, `pageSize`
- **响应数据字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 医生ID |
| name | string | 姓名 |
| title | string | 职称（主任医师/副主任医师等） |
| deptId | int | 科室ID |
| deptName | string | 科室名称 |
| avatar | string | 头像URL |
| specialty | string | 擅长领域 |
| introduction | string | 个人简介 |
| status | int | 0禁用 1启用 |

### 4.2 新增医生

- **POST** `/doctor`

### 4.3 编辑医生

- **PUT** `/doctor/{id}`

### 4.4 删除医生

- **DELETE** `/doctor/{id}`

### 4.5 重置医生密码

- **POST** `/doctor/{id}/reset-password`
- **响应：**

```json
{
  "code": 200,
  "data": {
    "newPassword": "123456"
  }
}
```

---

## 五、排班管理

### 5.1 排班列表

- **GET** `/schedule/list`
- **查询参数：** `doctorId`, `deptId`, `startDate`, `endDate`, `page`, `pageSize`
- **响应数据字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 排班ID |
| doctorId | int | 医生ID |
| doctorName | string | 医生姓名 |
| deptName | string | 科室名称 |
| date | string | 日期（YYYY-MM-DD） |
| period | string | 时段：morning/afternoon/evening |
| quota | int | 放号数量 |
| booked | int | 已预约数 |
| status | int | 0休息 1出诊 |

### 5.2 新增排班

- **POST** `/schedule`
- **请求体：**

```json
{
  "doctorId": 1,
  "date": "2024-06-01",
  "period": "morning",
  "quota": 30,
  "status": 1
}
```

### 5.3 编辑排班

- **PUT** `/schedule/{id}`

### 5.4 删除排班

- **DELETE** `/schedule/{id}`

### 5.5 批量新增排班

- **POST** `/schedule/batch`
- **请求体：** 排班对象数组

### 5.6 批量删除排班

- **POST** `/schedule/batch-delete`
- **请求体：**

```json
{
  "ids": [1, 2, 3]
}
```

---

## 六、用户（患者）管理

### 6.1 用户列表

- **GET** `/user/list`
- **查询参数：** `keyword`, `status`, `page`, `pageSize`
- **响应数据字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 用户ID |
| name | string | 姓名 |
| phone | string | 手机号 |
| idCard | string | 身份证号 |
| gender | int | 0女 1男 2未知 |
| age | int | 年龄 |
| status | int | 0禁用 1正常 |
| createTime | string | 注册时间 |

### 6.2 新增用户

- **POST** `/user`

### 6.3 编辑用户

- **PUT** `/user/{id}`

### 6.4 删除用户

- **DELETE** `/user/{id}`

### 6.5 重置用户密码

- **POST** `/user/{id}/reset-password`

---

## 七、预约管理

### 7.1 预约列表

- **GET** `/appointment/list`
- **查询参数：** `keyword`, `doctorId`, `deptId`, `status`, `startDate`, `endDate`, `page`, `pageSize`
- **响应数据字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 预约ID |
| patientName | string | 患者姓名 |
| patientPhone | string | 患者电话 |
| doctorName | string | 医生姓名 |
| deptName | string | 科室名称 |
| date | string | 预约日期 |
| period | string | 时段 |
| status | int | 0待就诊 1已就诊 2已取消 |
| createTime | string | 预约时间 |

### 7.2 新增预约

- **POST** `/appointment`

### 7.3 编辑预约

- **PUT** `/appointment/{id}`

### 7.4 删除预约

- **DELETE** `/appointment/{id}`

### 7.5 叫号

- **POST** `/appointment/{id}/call`
- **说明：** 将该预约状态更新为"已就诊"

---

## 八、就诊管理

### 8.1 就诊列表

- **GET** `/visit/list`
- **查询参数：** `keyword`, `doctorId`, `page`, `pageSize`
- **响应数据字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 就诊ID |
| patientName | string | 患者姓名 |
| doctorName | string | 医生姓名 |
| deptName | string | 科室名称 |
| diagnosis | string | 诊断结果 |
| advice | string | 医嘱 |
| visitTime | string | 就诊时间 |
| status | int | 0进行中 1已完成 |

### 8.2 新增就诊记录

- **POST** `/visit`

### 8.3 编辑就诊记录

- **PUT** `/visit/{id}`

### 8.4 获取就诊详情

- **GET** `/visit/{id}`

---

## 九、检查管理

### 9.1 检查列表

- **GET** `/exam/list`
- **查询参数：** `keyword`, `patientName`, `status`, `page`, `pageSize`
- **响应数据字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 检查ID |
| patientName | string | 患者姓名 |
| doctorName | string | 医生姓名 |
| examType | string | 检查类型（CT/MRI/X光等） |
| result | string | 检查结果 |
| reportUrl | string | 报告文件URL |
| status | int | 0待检查 1已完成 |
| createTime | string | 创建时间 |

### 9.2 新增检查

- **POST** `/exam`

### 9.3 编辑检查

- **PUT** `/exam/{id}`

### 9.4 上传检查报告

- **POST** `/exam/{id}/report`
- **Content-Type：** `multipart/form-data`
- **字段：** `file`（文件）

---

## 十、检验管理

### 10.1 检验列表

- **GET** `/lab/list`
- **查询参数：** 同检查管理
- **响应数据字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 检验ID |
| patientName | string | 患者姓名 |
| doctorName | string | 医生姓名 |
| labType | string | 检验类型（血常规/尿常规等） |
| result | string | 检验结果 |
| reportUrl | string | 报告文件URL |
| status | int | 0待检验 1已完成 |
| createTime | string | 创建时间 |

### 10.2 新增检验

- **POST** `/lab`

### 10.3 编辑检验

- **PUT** `/lab/{id}`

### 10.4 上传检验报告

- **POST** `/lab/{id}/report`
- **Content-Type：** `multipart/form-data`

---

## 十一、数据统计

### 11.1 首页统计数据

- **GET** `/statistics/dashboard`
- **响应：**

```json
{
  "code": 200,
  "data": {
    "doctorCount": 56,
    "deptCount": 12,
    "appointmentCount": 1280,
    "visitCount": 980,
    "todayAppointment": 45,
    "todayVisit": 38
  }
}
```

### 11.2 预约趋势图

- **GET** `/statistics/appointment-trend`
- **查询参数：** `startDate`, `endDate`
- **响应：**

```json
{
  "code": 200,
  "data": {
    "dates": ["2024-06-01", "2024-06-02", "2024-06-03"],
    "appointments": [30, 45, 28],
    "visits": [25, 40, 22]
  }
}
```

### 11.3 科室预约占比

- **GET** `/statistics/dept-ratio`
- **响应：**

```json
{
  "code": 200,
  "data": [
    { "name": "内科", "value": 350 },
    { "name": "外科", "value": 280 }
  ]
}
```

### 11.4 医生接诊量排行

- **GET** `/statistics/doctor-ranking`
- **查询参数：** `limit`（默认10）
- **响应：**

```json
{
  "code": 200,
  "data": [
    { "name": "张医生", "count": 156 },
    { "name": "李医生", "count": 132 }
  ]
}
```

---

## 十二、系统管理

### 12.1 菜单管理

- **GET** `/system/menu/list` - 菜单列表（树形）
- **POST** `/system/menu` - 新增菜单
- **PUT** `/system/menu/{id}` - 编辑菜单
- **DELETE** `/system/menu/{id}` - 删除菜单

**菜单字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 菜单ID |
| name | string | 菜单名称 |
| path | string | 路由路径 |
| component | string | 组件路径 |
| icon | string | 图标名称 |
| parentId | int/null | 父菜单ID |
| sort | int | 排序 |
| hidden | boolean | 是否隐藏 |
| permission | string | 权限标识 |

### 12.2 角色管理

- **GET** `/system/role/list` - 角色列表
- **POST** `/system/role` - 新增角色
- **PUT** `/system/role/{id}` - 编辑角色
- **DELETE** `/system/role/{id}` - 删除角色

**角色字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 角色ID |
| name | string | 角色名称 |
| code | string | 角色编码 |
| description | string | 描述 |
| menuIds | int[] | 关联菜单ID数组 |
| status | int | 0禁用 1启用 |

### 12.3 资讯管理

- **GET** `/system/news/list` - 资讯列表
- **POST** `/system/news` - 新增资讯
- **PUT** `/system/news/{id}` - 编辑资讯
- **DELETE** `/system/news/{id}` - 删除资讯

**资讯字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 资讯ID |
| title | string | 标题 |
| content | string | 内容（HTML） |
| cover | string | 封面图URL |
| recommended | boolean | 是否推荐 |
| status | int | 0下架 1上架 |
| createTime | string | 创建时间 |

### 12.4 公告管理

- **GET** `/system/notice/list` - 公告列表
- **POST** `/system/notice` - 新增公告
- **PUT** `/system/notice/{id}` - 编辑公告
- **DELETE** `/system/notice/{id}` - 删除公告

**公告字段：** 同资讯管理（showHome替代recommended）

### 12.5 反馈管理

- **GET** `/system/feedback/list` - 反馈列表
- **GET** `/system/feedback/{id}` - 反馈详情
- **DELETE** `/system/feedback/{id}` - 删除反馈

**反馈字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 反馈ID |
| userName | string | 用户姓名 |
| content | string | 反馈内容 |
| contact | string | 联系方式 |
| status | int | 0未处理 1已处理 |
| createTime | string | 创建时间 |

---

## 十三、AI 报告分析

### 13.1 请求 AI 分析

- **POST** `/ai/analyze`
- **请求体：**

```json
{
  "reportType": "exam",   // exam 或 lab
  "reportId": 1,
  "content": "检查报告文本内容..."
}
```

- **响应：**

```json
{
  "code": 200,
  "data": {
    "analysis": "AI分析结果文本...",
    "suggestions": ["建议1", "建议2"],
    "riskLevel": "low"   // low/medium/high
  }
}
```

---

## 十四、Mock 数据说明

前端已内置 Mock 拦截器，开发阶段无需真实后端即可运行。后端开发完成后，移除 Mock 配置即可对接真实接口。

Mock 数据文件位置：`src/api/mock.ts`

---

## 附录：状态码对照表

| 状态码 | 含义 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或Token过期 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
