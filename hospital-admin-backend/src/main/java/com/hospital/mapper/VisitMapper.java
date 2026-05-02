package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.Visit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VisitMapper extends BaseMapper<Visit> {

    Page<Visit> selectVisitPage(Page<Visit> page, @Param("keyword") String keyword, @Param("doctorId") Long doctorId);
}
