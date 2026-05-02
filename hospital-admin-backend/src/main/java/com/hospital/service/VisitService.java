package com.hospital.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.Visit;

public interface VisitService extends IService<Visit> {

    Page<Visit> getVisitPage(Long page, Long pageSize, String keyword, Long doctorId);
}
