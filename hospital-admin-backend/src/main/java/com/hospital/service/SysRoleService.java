package com.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    boolean assignMenus(Integer roleId, List<Integer> menuIds);
}
