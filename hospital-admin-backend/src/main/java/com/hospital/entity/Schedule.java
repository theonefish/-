package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("schedule")
public class Schedule {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long doctorId;
    private LocalDate date;
    private String period; // morning/afternoon/evening
    private Integer quota;
    private Integer booked;
    private Integer status; // 0休息 1出诊
    @TableField(exist = false)
    private String doctorName;
    @TableField(exist = false)
    private String deptName;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
