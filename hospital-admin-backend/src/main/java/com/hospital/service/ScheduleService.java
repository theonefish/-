package com.hospital.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.Schedule;

import java.util.List;

public interface ScheduleService extends IService<Schedule> {

    Page<Schedule> getSchedulePage(Long page, Long pageSize, Long doctorId, Long deptId,
                                    String startDate, String endDate);

    boolean batchCreate(List<Schedule> schedules);

    boolean batchDelete(List<Long> ids);
}
