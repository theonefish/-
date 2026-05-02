package com.hospital.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.Exam;

public interface ExamService extends IService<Exam> {

    Page<Exam> getExamPage(Long page, Long pageSize, String keyword, String patientName, Integer status);
}
