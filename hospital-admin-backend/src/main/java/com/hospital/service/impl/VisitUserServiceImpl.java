package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.VisitUser;
import com.hospital.mapper.VisitUserMapper;
import com.hospital.service.VisitUserService;
import org.springframework.stereotype.Service;

@Service
public class VisitUserServiceImpl extends ServiceImpl<VisitUserMapper, VisitUser> implements VisitUserService {
}
