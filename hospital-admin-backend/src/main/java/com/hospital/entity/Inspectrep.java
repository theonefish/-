package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("inspectrep")
public class Inspectrep {
    @TableId(value = "inspectrep_id", type = IdType.AUTO)
    private Integer inspectrepId;
    private Integer makeId;
    private Integer userId;
    private Integer visitUserId;
    private Integer doctorId;
    private String times;
    private String timesArea;
    private String week;
    private String hasInspectrep;
    private String insAdvice;
    private LocalDateTime insTime;
    private LocalDateTime createTime;
}
