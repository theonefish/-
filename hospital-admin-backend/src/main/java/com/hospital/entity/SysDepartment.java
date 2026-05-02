package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_department")
public class SysDepartment {
    @TableId(value = "dept_id", type = IdType.AUTO)
    private Integer deptId;
    private String deptName;
    private Integer orderNum;
    private String phone;
    private String toHome;

    // 数据库中暂无此字段，标记为不存在，避免 SQL 报错
    // 后续如需持久化，可执行：ALTER TABLE sys_department ADD COLUMN dept_desc VARCHAR(100);
    @TableField(exist = false)
    private String deptDesc;
}
