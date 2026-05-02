package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.Suggest;
import com.hospital.mapper.SuggestMapper;
import com.hospital.service.SuggestService;
import org.springframework.stereotype.Service;

@Service
public class SuggestServiceImpl extends ServiceImpl<SuggestMapper, Suggest> implements SuggestService {
}
