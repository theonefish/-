package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_notice")
public class SysNotice {
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Integer noticeId;
    private String noticeTitle;
    private String noticeText;
    private LocalDateTime createTime;
}
