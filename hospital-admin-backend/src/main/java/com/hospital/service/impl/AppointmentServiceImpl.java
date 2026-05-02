package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.Appointment;
import com.hospital.entity.Schedule;
import com.hospital.entity.Visit;
import com.hospital.mapper.AppointmentMapper;
import com.hospital.mapper.ScheduleMapper;
import com.hospital.mapper.VisitMapper;
import com.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private VisitMapper visitMapper;

    @Override
    public Page<Appointment> getAppointmentPage(Long page, Long pageSize, String keyword, Long doctorId,
                                                 Long deptId, Integer status, String startDate, String endDate) {
        Page<Appointment> pageParam = new Page<>(page, pageSize);
        return baseMapper.selectAppointmentPage(pageParam, keyword, doctorId, deptId, status, startDate, endDate);
    }

    @Override
    @Transactional
    public boolean callNumber(Long appointmentId) {
        Appointment appointment = getById(appointmentId);
        if (appointment == null) return false;

        appointment.setStatus(1);
        boolean updated = updateById(appointment);

        // 创建就诊记录
        Visit visit = new Visit();
        visit.setAppointmentId(appointmentId);
        visit.setPatientId(appointment.getPatientId());
        visit.setPatientName(appointment.getPatientName());
        visit.setDoctorId(appointment.getDoctorId());
        visit.setDeptId(appointment.getDeptId());
        visit.setVisitTime(LocalDateTime.now());
        visit.setStatus(0);
        visitMapper.insert(visit);

        return updated;
    }

    @Override
    @Transactional
    public boolean save(Appointment appointment) {
        return super.save(appointment);
    }
}
