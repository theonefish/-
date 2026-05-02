package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("make_order")
public class MakeOrder {
    @TableId(value = "make_id", type = IdType.AUTO)
    private Integer makeId;
    private Integer userId;
    private Integer scheduleId;
    private Integer visitUserId;
    private Integer doctorId;
    private Integer deptId;
    private String times;
    private String timesArea;
    private String week;
    private LocalDateTime createTime;
    private BigDecimal price;
    private String address;
    private String status;
    private String hasCall;
    private String hasVisit;
    private String hasProbe;
    private String hasProberep;
    private String hasInspect;
    private String hasInspectrep;
}
