package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.Doctor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {

    @Select("SELECT d.*, dept.name as dept_name FROM doctor d " +
            "LEFT JOIN department dept ON d.dept_id = dept.id " +
            "WHERE d.deleted = 0 AND dept.deleted = 0 " +
            "ORDER BY d.create_time DESC")
    List<Doctor> selectDoctorList();

    Page<Doctor> selectDoctorPage(Page<Doctor> page, @Param("keyword") String keyword,
                                   @Param("deptId") Long deptId, @Param("status") Integer status);
}
