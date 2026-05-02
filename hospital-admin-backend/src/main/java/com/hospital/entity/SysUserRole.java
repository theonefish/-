package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user_role")
public class SysUserRole {
    @TableId(value = "user_role_id", type = IdType.AUTO)
    private Integer userRoleId;
    private Integer roleId;
    private Integer userId;
}
