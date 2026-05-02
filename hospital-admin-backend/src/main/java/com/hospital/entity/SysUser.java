package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String sex;
    private String isAdmin;
    private Integer isEnabled;
    private String nickName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deptId;
    private String education;
    private String jobTitle;
    private String image;
    private String introduction;
    private String visitAddress;
    private String toHome;
    private String goodAt;
    private BigDecimal price;
    private Integer isAccountNonExpired;
    private Integer isAccountNonLocked;
    private Integer isCredentialsNonExpired;
}
