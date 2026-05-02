package com.hospital.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.Lab;

public interface LabService extends IService<Lab> {

    Page<Lab> getLabPage(Long page, Long pageSize, String keyword, String patientName, Integer status);
}
