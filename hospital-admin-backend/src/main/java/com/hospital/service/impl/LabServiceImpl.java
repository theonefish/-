package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.Lab;
import com.hospital.mapper.LabMapper;
import com.hospital.service.LabService;
import org.springframework.stereotype.Service;

@Service
public class LabServiceImpl extends ServiceImpl<LabMapper, Lab> implements LabService {

    @Override
    public Page<Lab> getLabPage(Long page, Long pageSize, String keyword, String patientName, Integer status) {
        Page<Lab> pageParam = new Page<>(page, pageSize);
        return baseMapper.selectLabPage(pageParam, keyword, patientName, status);
    }
}
