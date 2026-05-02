package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.Proberep;
import com.hospital.mapper.ProberepMapper;
import com.hospital.service.ProberepService;
import org.springframework.stereotype.Service;

@Service
public class ProberepServiceImpl extends ServiceImpl<ProberepMapper, Proberep> implements ProberepService {
}
