package com.hospital.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.Appointment;

public interface AppointmentService extends IService<Appointment> {

    Page<Appointment> getAppointmentPage(Long page, Long pageSize, String keyword, Long doctorId,
                                          Long deptId, Integer status, String startDate, String endDate);

    boolean callNumber(Long appointmentId);
}
