package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.Exam;
import com.hospital.mapper.ExamMapper;
import com.hospital.service.ExamService;
import org.springframework.stereotype.Service;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Override
    public Page<Exam> getExamPage(Long page, Long pageSize, String keyword, String patientName, Integer status) {
        Page<Exam> pageParam = new Page<>(page, pageSize);
        return baseMapper.selectExamPage(pageParam, keyword, patientName, status);
    }
}
