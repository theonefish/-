package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {

    Page<Appointment> selectAppointmentPage(Page<Appointment> page, @Param("keyword") String keyword,
                                             @Param("doctorId") Long doctorId, @Param("deptId") Long deptId,
                                             @Param("status") Integer status, @Param("startDate") String startDate,
                                             @Param("endDate") String endDate);
}
