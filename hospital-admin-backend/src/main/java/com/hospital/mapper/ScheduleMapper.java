package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    Page<Schedule> selectSchedulePage(Page<Schedule> page, @Param("doctorId") Long doctorId,
                                       @Param("deptId") Long deptId, @Param("startDate") String startDate,
                                       @Param("endDate") String endDate);
}
