package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.MakeOrder;
import com.hospital.entity.ScheduleDetail;
import com.hospital.entity.VisitOrder;
import com.hospital.mapper.MakeOrderMapper;
import com.hospital.mapper.ScheduleDetailMapper;
import com.hospital.mapper.VisitOrderMapper;
import com.hospital.service.MakeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MakeOrderServiceImpl extends ServiceImpl<MakeOrderMapper, MakeOrder> implements MakeOrderService {

    @Autowired
    private ScheduleDetailMapper scheduleDetailMapper;

    @Autowired
    private VisitOrderMapper visitOrderMapper;

    @Override
    @Transactional
    public boolean callNumber(Integer makeId) {
        MakeOrder order = getById(makeId);
        if (order == null) return false;

        order.setHasCall("1");
        order.setHasVisit("1");
        boolean updated = updateById(order);

        // 创建就诊记录
        VisitOrder visit = new VisitOrder();
        visit.setMakeId(makeId);
        visit.setUserId(order.getUserId());
        visit.setVisitUserId(order.getVisitUserId());
        visit.setDoctorId(order.getDoctorId());
        visit.setTimes(order.getTimes());
        visit.setTimesArea(order.getTimesArea());
        visit.setWeek(order.getWeek());
        visit.setHasVisit("0");
        visit.setVisitTime(LocalDateTime.now());
        visit.setCreateTime(LocalDateTime.now());
        visitOrderMapper.insert(visit);

        return updated;
    }

    @Override
    @Transactional
    public boolean save(MakeOrder order) {
        // 扣减号源
        ScheduleDetail schedule = scheduleDetailMapper.selectById(order.getScheduleId());
        if (schedule != null && schedule.getLastAmount() != null && schedule.getLastAmount() > 0) {
            schedule.setLastAmount(schedule.getLastAmount() - 1);
            scheduleDetailMapper.updateById(schedule);
        }
        return super.save(order);
    }
}
