package com.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.MakeOrder;

public interface MakeOrderService extends IService<MakeOrder> {

    boolean callNumber(Integer makeId);
}
