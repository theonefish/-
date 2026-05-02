package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.Appointment;
import com.hospital.entity.Visit;
import com.hospital.mapper.AppointmentMapper;
import com.hospital.mapper.VisitMapper;
import com.hospital.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitServiceImpl extends ServiceImpl<VisitMapper, Visit> implements VisitService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public Page<Visit> getVisitPage(Long page, Long pageSize, String keyword, Long doctorId) {
        Page<Visit> pageParam = new Page<>(page, pageSize);
        return baseMapper.selectVisitPage(pageParam, keyword, doctorId);
    }

    @Override
    @Transactional
    public boolean updateById(Visit visit) {
        boolean result = super.updateById(visit);
        // 更新预约状态为已完成
        if (visit.getAppointmentId() != null && visit.getStatus() != null && visit.getStatus() == 1) {
            Appointment appointment = appointmentMapper.selectById(visit.getAppointmentId());
            if (appointment != null) {
                appointment.setStatus(1);
                appointmentMapper.updateById(appointment);
            }
        }
        return result;
    }
}
