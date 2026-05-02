package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("wx_user")
public class WxUser {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;
    private String userName;
    private String nickName;
    private String phone;
    private String sex;
    private String name;
    private String image;
    private Integer status;
    private String password;
    private LocalDateTime createTime;
}
