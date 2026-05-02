package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.Schedule;
import com.hospital.mapper.ScheduleMapper;
import com.hospital.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

    @Override
    public Page<Schedule> getSchedulePage(Long page, Long pageSize, Long doctorId, Long deptId,
                                           String startDate, String endDate) {
        Page<Schedule> pageParam = new Page<>(page, pageSize);
        return baseMapper.selectSchedulePage(pageParam, doctorId, deptId, startDate, endDate);
    }

    @Override
    @Transactional
    public boolean batchCreate(List<Schedule> schedules) {
        return saveBatch(schedules);
    }

    @Override
    @Transactional
    public boolean batchDelete(List<Long> ids) {
        return removeByIds(ids);
    }
}
