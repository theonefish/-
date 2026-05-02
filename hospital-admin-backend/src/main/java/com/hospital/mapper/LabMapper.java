package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.Lab;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LabMapper extends BaseMapper<Lab> {

    Page<Lab> selectLabPage(Page<Lab> page, @Param("keyword") String keyword,
                             @Param("patientName") String patientName, @Param("status") Integer status);
}
