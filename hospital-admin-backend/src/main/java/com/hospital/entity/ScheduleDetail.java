package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("schedule_detail")
public class ScheduleDetail {
    @TableId(value = "schedule_id", type = IdType.AUTO)
    private Integer scheduleId;
    private Integer doctorId;
    private String doctorName;
    private String times;
    private String week;
    private Integer witchWeek;
    private Integer amount;
    private String type;
    private Integer lastAmount;
}
