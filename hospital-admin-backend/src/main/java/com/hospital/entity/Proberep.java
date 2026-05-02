package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("proberep")
public class Proberep {
    @TableId(value = "proberep_id", type = IdType.AUTO)
    private Integer proberepId;
    private Integer makeId;
    private Integer userId;
    private Integer visitUserId;
    private Integer doctorId;
    private String times;
    private String timesArea;
    private String week;
    private String hasProberep;
    private String proAdvice;
    private LocalDateTime proTime;
    private LocalDateTime createTime;
}
