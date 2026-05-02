package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.MakeOrder;
import com.hospital.entity.VisitOrder;
import com.hospital.mapper.MakeOrderMapper;
import com.hospital.mapper.VisitOrderMapper;
import com.hospital.service.VisitOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitOrderServiceImpl extends ServiceImpl<VisitOrderMapper, VisitOrder> implements VisitOrderService {

    @Autowired
    private MakeOrderMapper makeOrderMapper;

    @Override
    @Transactional
    public boolean updateById(VisitOrder visit) {
        boolean result = super.updateById(visit);
        // 更新预约状态
        if (visit.getMakeId() != null && "1".equals(visit.getHasVisit())) {
            MakeOrder order = makeOrderMapper.selectById(visit.getMakeId());
            if (order != null) {
                order.setHasVisit("1");
                makeOrderMapper.updateById(order);
            }
        }
        return result;
    }
}
