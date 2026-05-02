package com.hospital.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.Doctor;

public interface DoctorService extends IService<Doctor> {

    Page<Doctor> getDoctorPage(Long page, Long pageSize, String keyword, Long deptId, Integer status);

    boolean resetPassword(Long doctorId);
}
