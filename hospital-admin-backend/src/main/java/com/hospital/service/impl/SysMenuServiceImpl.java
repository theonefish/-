package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.entity.SysMenu;
import com.hospital.mapper.SysMenuMapper;
import com.hospital.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> getMenusByRoleId(Integer roleId) {
        return baseMapper.selectMenusByRoleId(roleId);
    }

    @Override
    public List<SysMenu> getParentMenus() {
        return baseMapper.selectParentMenus();
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> tree = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (menu.getParentId() == null) {
                tree.add(findChildren(menu, menus));
            }
        }
        return tree;
    }

    private SysMenu findChildren(SysMenu menu, List<SysMenu> menus) {
        List<SysMenu> children = menus.stream()
                .filter(m -> menu.getMenuId().equals(m.getParentId()))
                .map(m -> findChildren(m, menus))
                .collect(Collectors.toList());
        menu.setChildren(children);
        return menu;
    }
}
