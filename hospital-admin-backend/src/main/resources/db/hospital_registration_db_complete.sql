-- =====================================================
-- 医院预约挂号小程序 MySQL 数据库完整脚本
-- 包含：创建数据库 + 建表 + 初始数据
-- =====================================================

-- 先创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS hospital_registration_db
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_general_ci;

-- 使用数据库
USE hospital_registration_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 科室信息表
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `dept_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '科室标识符，主键，自增',
  `dept_name` varchar(36) DEFAULT NULL COMMENT '科室名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序序号',
  `phone` varchar(11) DEFAULT NULL COMMENT '科室联系电话',
  `to_home` varchar(2) DEFAULT NULL COMMENT '是否推荐到首页',
  `dept_desc` varchar(100) DEFAULT NULL COMMENT '科室简介描述',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医院科室信息表';

-- ----------------------------
-- 2. 系统菜单权限表
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单标识符，主键，自增',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级菜单标识符',
  `title` varchar(36) DEFAULT NULL COMMENT '菜单名称',
  `code` varchar(36) NOT NULL COMMENT '权限标识字段',
  `name` varchar(36) DEFAULT NULL COMMENT '前端路由名称',
  `path` varchar(36) DEFAULT NULL COMMENT '前端路由地址',
  `url` varchar(128) DEFAULT NULL COMMENT '前端组件路径',
  `type` varchar(2) DEFAULT NULL COMMENT '菜单类型',
  `icon` varchar(36) DEFAULT NULL COMMENT '菜单图标',
  `parent_name` varchar(36) DEFAULT NULL COMMENT '上级菜单名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单与权限表';

-- ----------------------------
-- 菜单初始数据
-- ----------------------------
INSERT INTO `sys_menu` (`parent_id`, `title`, `code`, `name`, `path`, `url`, `type`, `icon`, `order_num`, `create_time`, `update_time`) VALUES
(0, '首页', 'dashboard:view', 'Dashboard', 'dashboard', '@/views/dashboard/index.vue', '1', 'HomeFilled', 1, NOW(), NOW()),
(0, '科室管理', 'department:view', 'Department', 'department', '@/views/department/index.vue', '1', 'OfficeBuilding', 2, NOW(), NOW()),
(0, '医生管理', 'doctor:view', 'Doctor', 'doctor', '@/views/doctor/index.vue', '1', 'UserFilled', 3, NOW(), NOW()),
(0, '排班管理', 'schedule:view', 'Schedule', 'schedule', '@/views/schedule/index.vue', '1', 'Calendar', 4, NOW(), NOW()),
(0, '用户管理', 'user:view', 'User', 'user', '@/views/user/index.vue', '1', 'User', 5, NOW(), NOW()),
(0, '预约管理', 'appointment:view', 'Appointment', 'appointment', '@/views/appointment/index.vue', '1', 'Timer', 6, NOW(), NOW()),
(0, '就诊管理', 'visit:view', 'Visit', 'visit', '@/views/visit/index.vue', '1', 'FirstAidKit', 7, NOW(), NOW()),
(0, '检查管理', 'exam:view', 'Exam', 'exam', '@/views/exam/index.vue', '1', 'View', 8, NOW(), NOW()),
(0, '检验管理', 'lab:view', 'Lab', 'lab', '@/views/lab/index.vue', '1', 'Collection', 9, NOW(), NOW()),
(0, '数据统计', 'statistics:view', 'Statistics', 'statistics', '@/views/statistics/index.vue', '1', 'TrendCharts', 10, NOW(), NOW()),
(0, '系统管理', 'system:menu:view', 'System', 'system', '', '1', 'Setting', 11, NOW(), NOW()),
((SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE path='system' LIMIT 1) t), '菜单管理', 'system:menu:view', 'Menu', 'menu', '@/views/system/menu.vue', '2', 'Menu', 1, NOW(), NOW()),
((SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE path='system' LIMIT 1) t), '角色管理', 'system:role:view', 'Role', 'role', '@/views/system/role.vue', '2', 'Lock', 2, NOW(), NOW()),
((SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE path='system' LIMIT 1) t), '资讯管理', 'system:news:view', 'News', 'news', '@/views/system/news.vue', '2', 'Document', 3, NOW(), NOW()),
((SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE path='system' LIMIT 1) t), '公告管理', 'system:notice:view', 'Notice', 'notice', '@/views/system/notice.vue', '2', 'Bell', 4, NOW(), NOW()),
((SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE path='system' LIMIT 1) t), '反馈管理', 'system:feedback:view', 'Feedback', 'feedback', '@/views/system/feedback.vue', '2', 'ChatLineRound', 5, NOW(), NOW());

-- ----------------------------
-- 3. 系统角色表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色标识符，主键，自增',
  `role_name` varchar(36) DEFAULT NULL COMMENT '角色名称',
  `type` varchar(4) DEFAULT NULL COMMENT '角色类型',
  `remark` varchar(64) DEFAULT NULL COMMENT '角色备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- ----------------------------
-- 4. 角色菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色菜单关联标识符，主键，自增',
  `role_id` int(11) DEFAULT NULL COMMENT '角色标识符',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单标识符',
  PRIMARY KEY (`role_menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色与菜单多对多关联表';

-- ----------------------------
-- 5. 医生/管理员用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户标识符，主键，自增',
  `username` varchar(36) DEFAULT NULL COMMENT '登录账户',
  `password` varchar(128) DEFAULT NULL COMMENT '加密后的登录密码',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(36) DEFAULT NULL COMMENT '邮箱',
  `sex` varchar(4) DEFAULT NULL COMMENT '性别',
  `is_admin` varchar(2) DEFAULT NULL COMMENT '是否超级管理员',
  `is_enabled` tinyint(4) DEFAULT NULL COMMENT '账户是否可用',
  `nick_name` varchar(20) DEFAULT NULL COMMENT '医生/管理员姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `dept_id` int(11) DEFAULT NULL COMMENT '所属科室标识符',
  `education` varchar(36) DEFAULT NULL COMMENT '学历',
  `job_title` varchar(36) DEFAULT NULL COMMENT '职称',
  `image` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `introduction` text DEFAULT NULL COMMENT '医生介绍',
  `visit_address` varchar(255) DEFAULT NULL COMMENT '出诊地址',
  `to_home` varchar(2) DEFAULT NULL COMMENT '是否推荐到首页',
  `good_at` text DEFAULT NULL COMMENT '擅长方向',
  `price` decimal(10,0) DEFAULT NULL COMMENT '挂号费',
  `is_account_non_expired` tinyint(4) DEFAULT NULL COMMENT '账户是否过期',
  `is_account_non_locked` tinyint(4) DEFAULT NULL COMMENT '账户是否被锁定',
  `is_credentials_non_expired` tinyint(4) DEFAULT NULL COMMENT '密码是否过期',
  PRIMARY KEY (`user_id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台医生/管理员用户信息表';

-- ----------------------------
-- 6. 医生角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '医生角色关联标识符，主键，自增',
  `role_id` int(11) DEFAULT NULL COMMENT '角色标识符',
  `user_id` int(11) DEFAULT NULL COMMENT '医生用户标识符',
  PRIMARY KEY (`user_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生与角色多对多关联表';

-- ----------------------------
-- 7. 后台公告表
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告标识符，主键，自增',
  `notice_title` varchar(255) DEFAULT NULL COMMENT '公告标题',
  `notice_text` text DEFAULT NULL COMMENT '公告内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台公告信息表';

-- ----------------------------
-- 8. 小程序用户表
-- ----------------------------
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户标识符，主键，自增',
  `user_name` varchar(255) DEFAULT NULL COMMENT '登录账号',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `phone` varchar(18) DEFAULT NULL COMMENT '联系电话',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别',
  `name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `image` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `status` tinyint(4) DEFAULT NULL COMMENT '账号是否启用',
  `password` varchar(255) DEFAULT NULL COMMENT '加密后的登录密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序端用户信息表';

-- ----------------------------
-- 9. 就诊人信息表
-- ----------------------------
DROP TABLE IF EXISTS `visit_user`;
CREATE TABLE `visit_user` (
  `visit_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '就诊人标识符，主键，自增',
  `user_id` int(11) DEFAULT NULL COMMENT '绑定的小程序用户标识符',
  `visitname` varchar(255) DEFAULT NULL COMMENT '就诊人姓名',
  `sex` varchar(2) DEFAULT NULL COMMENT '就诊人性别',
  `birthday` varchar(255) DEFAULT NULL COMMENT '出生日期',
  `phone` varchar(20) DEFAULT NULL COMMENT '就诊人联系电话',
  `id_card` varchar(36) DEFAULT NULL COMMENT '就诊人身份证号码',
  PRIMARY KEY (`visit_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序用户绑定的就诊人信息表';

-- ----------------------------
-- 10. 医生排班号源表
-- ----------------------------
DROP TABLE IF EXISTS `schedule_detail`;
CREATE TABLE `schedule_detail` (
  `schedule_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '排班标识符，主键，自增',
  `doctor_id` int(11) DEFAULT NULL COMMENT '医生标识符',
  `doctor_name` varchar(255) DEFAULT NULL COMMENT '医生姓名',
  `times` varchar(36) DEFAULT NULL COMMENT '排班日期',
  `week` varchar(26) DEFAULT NULL COMMENT '星期几',
  `witch_week` int(255) DEFAULT NULL COMMENT '第几周',
  `amount` int(255) DEFAULT NULL COMMENT '总挂号数量',
  `type` varchar(2) DEFAULT NULL COMMENT '是否上班',
  `last_amount` int(255) DEFAULT NULL COMMENT '剩余可预约号数',
  PRIMARY KEY (`schedule_id`),
  KEY `idx_doctor_id` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生出诊排班与号源表';

-- ----------------------------
-- 11. 用户预约订单表
-- ----------------------------
DROP TABLE IF EXISTS `make_order`;
CREATE TABLE `make_order` (
  `make_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '预约标识符，主键，自增',
  `user_id` int(11) DEFAULT NULL COMMENT '预约人（小程序用户）标识符',
  `schedule_id` int(11) DEFAULT NULL COMMENT '排班标识符',
  `visit_user_id` int(11) DEFAULT NULL COMMENT '就诊人标识符',
  `doctor_id` int(11) DEFAULT NULL COMMENT '医生标识符',
  `dept_id` int(11) DEFAULT NULL COMMENT '科室标识符',
  `times` varchar(64) DEFAULT NULL COMMENT '预约时间',
  `times_area` varchar(2) DEFAULT NULL COMMENT '预约时段',
  `week` varchar(255) DEFAULT NULL COMMENT '星期',
  `create_time` datetime DEFAULT NULL COMMENT '预约订单创建时间',
  `price` decimal(10,0) DEFAULT NULL COMMENT '预约金额',
  `address` varchar(255) DEFAULT NULL COMMENT '就诊地址',
  `status` varchar(2) DEFAULT NULL COMMENT '预约状态',
  `has_call` varchar(2) DEFAULT NULL COMMENT '是否已叫号',
  `has_visit` varchar(2) DEFAULT NULL COMMENT '是否已就诊',
  `has_probe` varchar(2) DEFAULT NULL COMMENT '是否需要检查',
  `has_proberep` varchar(2) DEFAULT NULL COMMENT '是否已上传检查报告',
  `has_inspect` varchar(2) DEFAULT NULL COMMENT '是否需要检验',
  `has_inspectrep` varchar(2) DEFAULT NULL COMMENT '是否已上传检验报告',
  PRIMARY KEY (`make_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_schedule_id` (`schedule_id`),
  KEY `idx_doctor_id` (`doctor_id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户预约挂号订单表';

-- ----------------------------
-- 12. 就诊记录与医嘱表
-- ----------------------------
DROP TABLE IF EXISTS `visit_order`;
CREATE TABLE `visit_order` (
  `visit_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '就诊标识符，主键，自增',
  `make_id` int(11) DEFAULT NULL COMMENT '关联的预约标识符',
  `user_id` int(11) DEFAULT NULL COMMENT '预约人标识符',
  `visit_user_id` int(11) DEFAULT NULL COMMENT '就诊人标识符',
  `doctor_id` int(11) DEFAULT NULL COMMENT '医生标识符',
  `times` varchar(64) DEFAULT NULL COMMENT '预约时间',
  `times_area` varchar(2) DEFAULT NULL COMMENT '预约时段',
  `week` varchar(36) DEFAULT NULL COMMENT '星期',
  `has_visit` varchar(2) DEFAULT NULL COMMENT '是否已就诊',
  `has_live` varchar(2) DEFAULT NULL COMMENT '是否需要住院',
  `advice` text DEFAULT NULL COMMENT '医生医嘱',
  `visit_time` datetime DEFAULT NULL COMMENT '实际就诊时间',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`visit_id`),
  KEY `idx_make_id` (`make_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_doctor_id` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='就诊记录与医生医嘱表';

-- ----------------------------
-- 13. 检查报告表
-- ----------------------------
DROP TABLE IF EXISTS `proberep`;
CREATE TABLE `proberep` (
  `proberep_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '检查标识符，主键，自增',
  `make_id` int(11) DEFAULT NULL COMMENT '关联的预约标识符',
  `user_id` int(11) DEFAULT NULL COMMENT '预约人标识符',
  `visit_user_id` int(11) DEFAULT NULL COMMENT '就诊人标识符',
  `doctor_id` int(11) DEFAULT NULL COMMENT '医生标识符',
  `times` varchar(64) DEFAULT NULL COMMENT '预约时间',
  `times_area` varchar(2) DEFAULT NULL COMMENT '预约时段',
  `week` varchar(36) DEFAULT NULL COMMENT '星期',
  `has_proberep` varchar(2) DEFAULT NULL COMMENT '是否已上传检查报告',
  `pro_advice` text DEFAULT NULL COMMENT '检查报告内容',
  `pro_time` datetime DEFAULT NULL COMMENT '上传检查报告时间',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`proberep_id`),
  KEY `idx_make_id` (`make_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_doctor_id` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户检查报告信息表';

-- ----------------------------
-- 14. 检验报告表
-- ----------------------------
DROP TABLE IF EXISTS `inspectrep`;
CREATE TABLE `inspectrep` (
  `inspectrep_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '检验标识符，主键，自增',
  `make_id` int(11) DEFAULT NULL COMMENT '关联的预约标识符',
  `user_id` int(11) DEFAULT NULL COMMENT '预约人标识符',
  `visit_user_id` int(11) DEFAULT NULL COMMENT '就诊人标识符',
  `doctor_id` int(11) DEFAULT NULL COMMENT '医生标识符',
  `times` varchar(64) DEFAULT NULL COMMENT '预约时间',
  `times_area` varchar(2) DEFAULT NULL COMMENT '预约时段',
  `week` varchar(36) DEFAULT NULL COMMENT '星期',
  `has_inspectrep` varchar(2) DEFAULT NULL COMMENT '是否已上传检验报告',
  `ins_advice` text DEFAULT NULL COMMENT '检验报告内容',
  `ins_time` datetime DEFAULT NULL COMMENT '上传检验报告时间',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`inspectrep_id`),
  KEY `idx_make_id` (`make_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_doctor_id` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户检验报告信息表';

-- ----------------------------
-- 15. 健康资讯表
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` int(17) NOT NULL AUTO_INCREMENT COMMENT '资讯标识符，主键，自增',
  `title` varchar(255) DEFAULT NULL COMMENT '资讯标题',
  `text_desc` varchar(255) DEFAULT NULL COMMENT '资讯简介',
  `text_content` text DEFAULT NULL COMMENT '资讯内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `image` varchar(255) DEFAULT NULL COMMENT '资讯图片地址',
  `to_index` varchar(2) DEFAULT NULL COMMENT '是否设置到首页',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康资讯信息表';

-- 插入示例资讯数据
INSERT INTO `news` (`title`, `text_desc`, `text_content`, `create_time`, `image`, `to_index`) VALUES
('关于调整部分门诊挂号费用的通知及详细说明', '根据上级部门要求，我院将于下月起调整部分科室挂号费用，具体调整方案请查看详情。', '尊敬的各位患者：根据上级部门要求，我院将于2024年6月1日起调整部分科室挂号费用。具体调整方案如下：普通门诊挂号费由10元调整为12元，专家门诊挂号费由30元调整为35元。特此通知，感谢您的理解与支持。', NOW(), '/static/news1.jpg', '1'),
('春季流感高发期，这份防护指南请收好！', '春季是流感高发季节，呼吸内科王医生为您整理了详细的防护指南，帮助您和家人远离流感。', '春季流感高发期防护指南：1.勤洗手，保持手部卫生；2.戴口罩，避免去人群密集场所；3.多喝水，保持充足睡眠；4.如出现发热、咳嗽等症状，请及时就医。', DATE_SUB(NOW(), INTERVAL 1 DAY), '/static/news2.jpg', '1'),
('我院引进最新核磁共振设备，检查更精准', '为提升诊疗水平，我院最新引进3.0T核磁共振设备，成像更清晰，检查时间更短。', '我院最新引进西门子3.0T核磁共振设备，具有超高场强、超快速成像等特点，可为神经系统、骨关节等疾病的诊断提供更精准的影像支持。欢迎有需要的患者前来咨询预约。', DATE_SUB(NOW(), INTERVAL 2 DAY), '/static/news3.jpg', '1'),
('儿科专家联合坐诊，守护儿童健康', '本周六，我院儿科将邀请三甲医院专家联合坐诊，为儿童提供专业的诊疗服务。', '本周六（上午8:00-12:00），我院儿科将邀请市儿童医院主任医师张教授联合坐诊。擅长儿童呼吸系统、消化系统疾病的诊治。名额有限，请提前预约。', DATE_SUB(NOW(), INTERVAL 3 DAY), '/static/news4.jpg', '0');

-- ----------------------------
-- 16. 用户意见反馈表
-- ----------------------------
DROP TABLE IF EXISTS `suggest`;
CREATE TABLE `suggest` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '反馈标识符，主键，自增',
  `user_id` int(11) DEFAULT NULL COMMENT '反馈人标识符',
  `title` varchar(255) DEFAULT NULL COMMENT '反馈标题',
  `content` text DEFAULT NULL COMMENT '反馈内容',
  `create_time` datetime DEFAULT NULL COMMENT '反馈时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户意见反馈表';

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 初始数据插入
-- =====================================================

-- 插入超级管理员账号（密码：123456）
-- 密码加密方式：MD5(密码 + "hospital_salt_2024")
-- 123456 + hospital_salt_2024 的 MD5 = 4a49ead79bd4c5752ab4144e380d509a
INSERT INTO `sys_user` (`username`, `password`, `phone`, `email`, `sex`, `is_admin`, `is_enabled`, `nick_name`, `create_time`, `update_time`) 
VALUES ('admin', '4a49ead79bd4c5752ab4144e380d509a', '13800138000', 'admin@hospital.com', '男', '1', 1, '系统管理员', NOW(), NOW());

-- 插入示例科室
INSERT INTO `sys_department` (`dept_name`, `order_num`, `phone`, `to_home`, `dept_desc`) VALUES
('内科', 1, '01012345678', '1', '呼吸系统、消化系统疾病诊治'),
('外科', 2, '01012345679', '1', '普外、微创手术、创伤修复'),
('儿科', 3, '01012345680', '1', '儿童常见病、生长发育评估'),
('妇产科', 4, '01012345681', '1', '孕产保健、妇科疾病诊疗'),
('骨科', 5, '01012345682', '0', '关节置换、脊柱矫正、运动损伤'),
('眼科', 6, '01012345683', '0', '近视矫正、白内障、眼底病'),
('口腔科', 7, '01012345684', '0', '牙齿矫正、种植修复、牙周治疗'),
('皮肤科', 8, '01012345685', '0', '皮肤病、美容激光、过敏检测');

-- 插入示例医生数据（密码统一为 123456）
INSERT INTO `sys_user` (`username`, `password`, `phone`, `email`, `sex`, `is_admin`, `is_enabled`, `nick_name`, `create_time`, `update_time`, `dept_id`, `education`, `job_title`, `image`, `introduction`, `visit_address`, `to_home`, `good_at`, `price`) VALUES
('wangming', '4a49ead79bd4c5752ab4144e380d509a', '13800138001', 'wangming@hospital.com', '男', '0', 1, '王明', NOW(), NOW(), 1, '博士', '主任医师', '/static/doctor1.jpg', '从事内科临床工作20余年，擅长呼吸系统、消化系统疾病的诊断与治疗，曾在国内外知名医学期刊发表论文30余篇。', '内科门诊3楼A区', '1', '慢性咳嗽、哮喘、胃炎、消化性溃疡、肠易激综合征', 50),
('lili', '4a49ead79bd4c5752ab4144e380d509a', '13800138002', 'lili@hospital.com', '女', '0', 1, '李丽', NOW(), NOW(), 2, '博士', '副主任医师', '/static/doctor2.jpg', '外科临床工作15年，专注于微创手术技术，尤其在腹腔镜手术方面有丰富经验，手术成功率达99%以上。', '外科门诊2楼B区', '1', '胆囊切除、阑尾切除、疝气修补、甲状腺手术', 40),
('zhangwei', '4a49ead79bd4c5752ab4144e380d509a', '13800138003', 'zhangwei@hospital.com', '男', '0', 1, '张伟', NOW(), NOW(), 3, '硕士', '主任医师', '/static/doctor3.jpg', '儿科主任，从事儿科临床工作25年，对儿童呼吸系统疾病、过敏性疾病及儿童保健有深入研究，深受家长信赖。', '儿科门诊1楼C区', '1', '小儿肺炎、哮喘、过敏性疾病、生长发育迟缓', 45),
('liuhua', '4a49ead79bd4c5752ab4144e380d509a', '13800138004', 'liuhua@hospital.com', '女', '0', 1, '刘华', NOW(), NOW(), 4, '博士', '副主任医师', '/static/doctor4.jpg', '妇产科资深专家，擅长高危妊娠管理、产前诊断及妇科微创手术，帮助数千个家庭迎来健康宝宝。', '妇产科门诊3楼D区', '1', '高危妊娠、产前诊断、子宫肌瘤、卵巢囊肿', 40),
('chengang', '4a49ead79bd4c5752ab4144e380d509a', '13800138005', 'chengang@hospital.com', '男', '0', 1, '陈刚', NOW(), NOW(), 5, '硕士', '主治医师', '/static/doctor5.jpg', '骨科骨干医生，擅长运动损伤、关节疾病的诊断与治疗，曾赴德国进修关节置换技术。', '骨科门诊2楼E区', '0', '骨折、关节置换、运动损伤、腰椎间盘突出', 35),
('zhaoyan', '4a49ead79bd4c5752ab4144e380d509a', '13800138006', 'zhaoyan@hospital.com', '女', '0', 1, '赵燕', NOW(), NOW(), 6, '硕士', '副主任医师', '/static/doctor6.jpg', '眼科专家，专注近视防控、白内障手术及眼底病诊治，引进国际先进眼科诊疗设备。', '眼科门诊4楼F区', '0', '近视矫正、白内障、青光眼、眼底病变', 40),
('suntao', '4a49ead79bd4c5752ab4144e380d509a', '13800138007', 'suntao@hospital.com', '男', '0', 1, '孙涛', NOW(), NOW(), 7, '博士', '主任医师', '/static/doctor7.jpg', '口腔科主任，从事口腔临床工作22年，在种植牙、正畸及牙周病治疗方面有丰富经验。', '口腔科门诊2楼G区', '0', '种植牙、牙齿矫正、牙周病、根管治疗', 50),
('zhoufang', '4a49ead79bd4c5752ab4144e380d509a', '13800138008', 'zhoufang@hospital.com', '女', '0', 1, '周芳', NOW(), NOW(), 8, '硕士', '主治医师', '/static/doctor8.jpg', '皮肤科医师，擅长各类皮肤病诊治及医学美容，在激光美容、注射美容方面有独特见解。', '皮肤科门诊3楼H区', '0', '湿疹、痤疮、银屑病、激光美容、注射美容', 35);

-- 插入示例公告
INSERT INTO `sys_notice` (`notice_title`, `notice_text`, `create_time`) VALUES
('系统上线通知', '医院预约挂号系统正式上线，欢迎使用！', NOW()),
('就诊须知', '请提前30分钟到达医院，携带身份证和医保卡。', NOW());
