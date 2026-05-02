package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("visit_user")
public class VisitUser {
    @TableId(value = "visit_id", type = IdType.AUTO)
    private Integer visitId;
    private Integer userId;
    private String visitname;
    private String sex;
    private String birthday;
    private String phone;
    private String idCard;
}
