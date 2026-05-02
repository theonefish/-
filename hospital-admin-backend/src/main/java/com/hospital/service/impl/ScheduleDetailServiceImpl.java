package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.ScheduleDetail;
import com.hospital.mapper.ScheduleDetailMapper;
import com.hospital.service.ScheduleDetailService;
import org.springframework.stereotype.Service;

@Service
public class ScheduleDetailServiceImpl extends ServiceImpl<ScheduleDetailMapper, ScheduleDetail> implements ScheduleDetailService {
}
