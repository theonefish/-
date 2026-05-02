package com.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    boolean assignMenus(Long roleId, List<Long> menuIds);
}
