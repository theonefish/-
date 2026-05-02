package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.Role;
import com.hospital.entity.RoleMenu;
import com.hospital.mapper.RoleMapper;
import com.hospital.mapper.RoleMenuMapper;
import com.hospital.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public boolean assignMenus(Long roleId, List<Long> menuIds) {
        // 删除旧关联
        roleMenuMapper.deleteByRoleId(roleId);
        // 新增关联
        if (menuIds != null && !menuIds.isEmpty()) {
            List<RoleMenu> list = menuIds.stream().map(menuId -> {
                RoleMenu rm = new RoleMenu();
                rm.setRoleId(roleId);
                rm.setMenuId(menuId);
                return rm;
            }).collect(Collectors.toList());
            for (RoleMenu rm : list) {
                roleMenuMapper.insert(rm);
            }
        }
        return true;
    }
}
