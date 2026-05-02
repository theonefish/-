package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExamMapper extends BaseMapper<Exam> {

    Page<Exam> selectExamPage(Page<Exam> page, @Param("keyword") String keyword,
                               @Param("patientName") String patientName, @Param("status") Integer status);
}
