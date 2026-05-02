# 医院预约挂号系统 - Spring Boot 后端

## 技术架构

| 组件 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.5 | 核心框架 |
| MyBatis-Plus | 3.5.6 | ORM框架 |
| MySQL | 8.x | 数据库 |
| Redis | 7.x | 缓存/会话 |
| JWT | 4.4.0 | 身份认证 |
| 微信小程序 SDK | 4.6.0 | 小程序对接 |

## 数据库适配说明

本项目已完全适配 `hospital_registration_db.sql` 数据库结构，包含以下16张表：

| 表名 | 说明 | 对应实体 |
|------|------|----------|
| sys_department | 科室信息表 | SysDepartment |
| sys_menu | 系统菜单权限表 | SysMenu |
| sys_role | 系统角色表 | SysRole |
| sys_role_menu | 角色菜单关联表 | SysRoleMenu |
| sys_user | 医生/管理员用户表 | SysUser |
| sys_user_role | 医生角色关联表 | SysUserRole |
| sys_notice | 后台公告表 | SysNotice |
| wx_user | 小程序用户表 | WxUser |
| visit_user | 就诊人信息表 | VisitUser |
| schedule_detail | 医生排班号源表 | ScheduleDetail |
| make_order | 用户预约订单表 | MakeOrder |
| visit_order | 就诊记录与医嘱表 | VisitOrder |
| proberep | 检查报告表 | Proberep |
| inspectrep | 检验报告表 | Inspectrep |
| news | 健康资讯表 | News |
| suggest | 用户意见反馈表 | Suggest |

## 项目结构

```
hospital-admin-backend/
├── src/main/java/com/hospital/
│   ├── HospitalApplication.java
│   ├── config/                       # 配置类
│   ├── controller/                   # 控制器层
│   │   ├── AuthController.java       # 认证（登录/注册/改密）
│   │   ├── DepartmentController.java # 科室管理
│   │   ├── DoctorController.java     # 医生管理
│   │   ├── ScheduleController.java   # 排班管理
│   │   ├── UserController.java       # 小程序用户管理
│   │   ├── AppointmentController.java# 预约管理
│   │   ├── VisitController.java      # 就诊管理
│   │   ├── ExamController.java       # 检查管理
│   │   ├── LabController.java        # 检验管理
│   │   ├── StatisticsController.java # 数据统计
│   │   ├── SystemController.java     # 系统管理（菜单/角色/公告/反馈）
│   │   ├── NewsController.java       # 资讯管理
│   │   ├── WxUserController.java     # 小程序用户接口
│   │   ├── VisitUserController.java  # 就诊人管理
│   │   ├── MakeOrderController.java  # 小程序预约接口
│   │   ├── SuggestController.java    # 意见反馈
│   │   ├── AiController.java         # AI分析
│   │   └── UploadController.java     # 文件上传
│   ├── service/                      # 业务层
│   ├── mapper/                       # 数据访问层
│   ├── entity/                       # 实体类（适配数据库表结构）
│   ├── vo/                           # 视图对象
│   ├── utils/                        # 工具类
│   ├── interceptor/                  # JWT拦截器
│   └── exception/                    # 全局异常处理
├── src/main/resources/
│   ├── mapper/                       # XML映射
│   └── application.yml               # 配置文件
└── pom.xml
```

## 快速开始

### 1. 环境准备

- JDK 17+
- MySQL 8.0+
- Redis 7.0+
- Maven 3.8+

### 2. 数据库初始化

```bash
mysql -u root -p < hospital_registration_db.sql
```

### 3. 修改配置

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hospital_registration_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 4. 运行项目

```bash
mvn spring-boot:run
```

### 5. 访问接口

- 基础路径：`http://localhost:8080/api`
- 登录接口：`POST /api/auth/login`

## 前后端 + 小程序适配

### 管理端前端

前端项目通过 `/api/**` 路径访问后端接口，已配置 CORS 跨域支持。

### 小程序端

小程序专用接口：

| 接口 | 路径 | 说明 |
|------|------|------|
| 微信登录 | POST `/auth/wx-login` | openId 登录 |
| 用户注册 | POST `/auth/wx-register` | 小程序用户注册 |
| 获取用户信息 | GET `/wx/user/info/{id}` | 查询用户信息 |
| 就诊人列表 | GET `/visit-user/list/{userId}` | 查询就诊人 |
| 添加就诊人 | POST `/visit-user` | 新增就诊人 |
| 用户预约 | GET `/make-order/user/{userId}` | 查询用户预约 |
| 提交预约 | POST `/make-order` | 创建预约订单 |
| 提交反馈 | POST `/suggest` | 意见反馈 |

小程序对接流程：
1. 小程序调用 `wx.login()` 获取 code
2. 后端通过 code 换取 openId
3. 调用 `/auth/wx-login` 或 `/auth/wx-register`
4. 获取 JWT Token，后续请求携带 `Authorization: Bearer <token>`
