package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.Doctor;
import com.hospital.entity.User;
import com.hospital.mapper.DoctorMapper;
import com.hospital.mapper.UserMapper;
import com.hospital.service.DoctorService;
import com.hospital.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<Doctor> getDoctorPage(Long page, Long pageSize, String keyword, Long deptId, Integer status) {
        Page<Doctor> pageParam = new Page<>(page, pageSize);
        return baseMapper.selectDoctorPage(pageParam, keyword, deptId, status);
    }

    @Override
    @Transactional
    public boolean resetPassword(Long doctorId) {
        Doctor doctor = getById(doctorId);
        if (doctor == null || doctor.getUserId() == null) return false;
        User user = userMapper.selectById(doctor.getUserId());
        if (user == null) return false;
        user.setPassword(Md5Util.encrypt("666666"));
        return userMapper.updateById(user) > 0;
    }
}
