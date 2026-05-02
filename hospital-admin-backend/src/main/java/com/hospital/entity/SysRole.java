package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_role")
public class SysRole {
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;
    private String roleName;
    private String type;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
