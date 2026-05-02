package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role_menu")
public class SysRoleMenu {
    @TableId(value = "role_menu_id", type = IdType.AUTO)
    private Integer roleMenuId;
    private Integer roleId;
    private Integer menuId;
}
