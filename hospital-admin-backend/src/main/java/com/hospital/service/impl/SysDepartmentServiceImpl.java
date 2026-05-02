package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.SysDepartment;
import com.hospital.mapper.SysDepartmentMapper;
import com.hospital.service.SysDepartmentService;
import org.springframework.stereotype.Service;

@Service
public class SysDepartmentServiceImpl extends ServiceImpl<SysDepartmentMapper, SysDepartment> implements SysDepartmentService {
}
