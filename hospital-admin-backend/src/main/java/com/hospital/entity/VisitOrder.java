package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("visit_order")
public class VisitOrder {
    @TableId(value = "visit_id", type = IdType.AUTO)
    private Integer visitId;
    private Integer makeId;
    private Integer userId;
    private Integer visitUserId;
    private Integer doctorId;
    private String times;
    private String timesArea;
    private String week;
    private String hasVisit;
    private String hasLive;
    private String advice;
    private LocalDateTime visitTime;
    private LocalDateTime createTime;
}
