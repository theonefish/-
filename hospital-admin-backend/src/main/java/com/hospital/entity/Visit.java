package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("visit")
public class Visit {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long appointmentId;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private Long deptId;
    private String diagnosis;
    private String advice;
    private LocalDateTime visitTime;
    private Integer status; // 0进行中 1已完成
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
