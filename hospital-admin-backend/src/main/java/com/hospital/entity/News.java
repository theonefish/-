package com.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("news")
public class News {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String textDesc;
    private String textContent;
    private LocalDateTime createTime;
    private String image;
    private String toIndex;
}
